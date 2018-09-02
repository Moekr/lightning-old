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
import com.moekr.lightning.data.dao.ArticleDAO;
import com.moekr.lightning.data.entity.Article;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleAnalyticsReportFetcher {
    private static final Log LOG = LogFactory.getLog(GoogleAnalyticsReportFetcher.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final GoogleAnalyticsConfiguration configuration;
    private final PlatformTransactionManager transactionManager;
    private final ArticleDAO articleDAO;

    private AnalyticsReporting reporting;
    private DateRange dateRange;
    private Dimension dimension;
    private Metric metric;

    @Autowired
    public GoogleAnalyticsReportFetcher(GoogleAnalyticsConfiguration configuration, PlatformTransactionManager transactionManager, ArticleDAO articleDAO) {
        this.configuration = configuration;
        this.transactionManager = transactionManager;
        this.articleDAO = articleDAO;
    }

    @PostConstruct
    private void initial() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = new NetHttpTransport.Builder()
                .trustCertificates(GoogleUtils.getCertificateTrustStore())
                .build();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(new File(configuration.getKey())), httpTransport, JSON_FACTORY)
                .createScoped(Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY));
        reporting = new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(configuration.getName())
                .build();
        dateRange = new DateRange().setStartDate(configuration.getBegin()).setEndDate("today");
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
                .setViewId(configuration.getViewId())
                .setDateRanges(Collections.singletonList(dateRange))
                .setMetrics(Collections.singletonList(metric))
                .setDimensions(Collections.singletonList(dimension));
        GetReportsRequest request = new GetReportsRequest().setReportRequests(Collections.singletonList(reportRequest));
        GetReportsResponse response = reporting.reports().batchGet(request).execute();
        Report report = response.getReports().get(0);
        for (ReportRow row : report.getData().getRows()) {
            String uri = row.getDimensions().get(0);
            int views = Integer.valueOf(row.getMetrics().get(0).getValues().get(0));
            Article article = null;
            if (StringUtils.startsWith(uri, "/article/")) {
                int id = Integer.valueOf(StringUtils.substringAfter(uri, "/article/"));
                article = articleDAO.findById(id);
            } else if (StringUtils.startsWith(uri, "/")) {
                String alias = StringUtils.substringAfter(uri, "/");
                article = articleDAO.findByAlias(alias);
            }
            if (article != null) {
                article.setViews(views);
                articleDAO.save(article);
            }
        }
    }
}
