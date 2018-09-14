package com.moekr.lightning.logic.analytics;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.*;
import com.moekr.lightning.data.entity.Article;
import com.moekr.lightning.data.repository.ArticleRepository;
import com.moekr.lightning.util.ArticleType;
import com.moekr.lightning.util.LightningProperties;
import com.moekr.lightning.util.LightningProperties.AnalyticsProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.regex.Pattern;

@Component
@EnableScheduling
public class AnalyticsReportFetcher {
    private static final Log LOG = LogFactory.getLog(AnalyticsReportFetcher.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final Pattern ARTICLE_PATTERN = Pattern.compile("^/article/[a-zA-Z0-9_-]+\\.html$");
    private static final Pattern PAGE_PATTERN = Pattern.compile("^/page/[a-zA-Z0-9_-]+\\.html$");

    private final AnalyticsProperties properties;
    private final PlatformTransactionManager transactionManager;
    private final ArticleRepository repository;

    private AnalyticsReporting reporting;
    private DateRange dateRange;
    private Dimension dimension;
    private Metric metric;

    @Autowired
    public AnalyticsReportFetcher(LightningProperties properties, PlatformTransactionManager transactionManager, ArticleRepository repository) {
        this.properties = properties.getAnalytics();
        this.transactionManager = transactionManager;
        this.repository = repository;
    }

    @PostConstruct
    private void initial() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = new NetHttpTransport.Builder()
                .trustCertificates(GoogleUtils.getCertificateTrustStore())
                .build();
        InputStream inputStream = new FileInputStream(new File(properties.getKeyFile()));
        GoogleCredential credential = GoogleCredential.fromStream(inputStream, httpTransport, JSON_FACTORY)
                .createScoped(Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY));
        reporting = new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(properties.getAppName())
                .build();
        dateRange = new DateRange().setStartDate(properties.getBeginDate()).setEndDate("today");
        dimension = new Dimension().setName("ga:pagepath");
        metric = new Metric().setExpression("ga:pageviews");
        new Thread(this::invokeFetchReport).start();
    }

    @Scheduled(cron = "0 0 3 * * *")
    private void invokeFetchReport() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            fetchReport();
            transactionManager.commit(transactionStatus);
        } catch (Throwable e) {
            LOG.error("Fetch GoogleAnalytics Report Fail: " + e.getMessage());
            transactionManager.rollback(transactionStatus);
        }
    }

    private void fetchReport() throws IOException {
        ReportRequest reportRequest = new ReportRequest()
                .setViewId(properties.getViewId())
                .setDateRanges(Collections.singletonList(dateRange))
                .setMetrics(Collections.singletonList(metric))
                .setDimensions(Collections.singletonList(dimension));
        GetReportsRequest request = new GetReportsRequest().setReportRequests(Collections.singletonList(reportRequest));
        GetReportsResponse response = reporting.reports().batchGet(request).execute();
        Report report = response.getReports().get(0);
        for (ReportRow row : report.getData().getRows()) {
            String uri = row.getDimensions().get(0);
            int views = Integer.valueOf(row.getMetrics().get(0).getValues().get(0));
            String alias = null;
            ArticleType type = null;
            if (ARTICLE_PATTERN.matcher(uri).matches()) {
                alias = uri.substring("/article/".length(), uri.length() - ".html".length());
                type = ArticleType.NORMAL;
            } else if (PAGE_PATTERN.matcher(uri).matches()) {
                alias = uri.substring("/page/".length(), uri.length() - ".html".length());
                type = ArticleType.PAGE;
            }
            if (alias != null) {
                Article article = repository.findByAliasAndType(alias, type);
                if (article != null) {
                    article.setViews(views);
                    repository.save(article);
                }
            }
        }
    }
}
