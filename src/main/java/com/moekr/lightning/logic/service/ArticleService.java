package com.moekr.lightning.logic.service;

import com.moekr.lightning.data.entity.Article;
import com.moekr.lightning.data.repository.ArticleRepository;
import com.moekr.lightning.data.search.ElasticsearchConfiguration;
import com.moekr.lightning.data.search.IndexedArticle;
import com.moekr.lightning.data.search.SearchRepository;
import com.moekr.lightning.logic.vo.ArticleVO;
import com.moekr.lightning.util.ArticleType;
import com.moekr.lightning.util.ToolKit;
import com.moekr.lightning.web.dto.ArticleDTO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private static final Sort SORT = Sort.by(Sort.Order.desc("id"));

    private final ArticleRepository repository;
    private final SearchRepository searchRepository;
    private final Client client;

    @Autowired
    public ArticleService(ArticleRepository repository, SearchRepository searchRepository, Client client) {
        this.repository = repository;
        this.searchRepository = searchRepository;
        this.client = client;
    }

    @PostConstruct
    private void indexAllArticles() {
        searchRepository.deleteAll();
        for (Article article : repository.findAll()) {
            searchRepository.index(article);
        }
    }

    @Transactional
    public ArticleVO createArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setModifiedAt(article.getCreatedAt());
        article = repository.save(article);
        searchRepository.index(article);
        return new ArticleVO(article);
    }

    public List<ArticleVO> retrieveArticles(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, SORT);
        return repository.findAll(pageable).map(ArticleVO::new).getContent();
    }

    public ArticleVO retrieveArticle(int articleId) {
        Article article = repository.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        return new ArticleVO(article);
    }

    public Page<ArticleVO> viewArticles(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, SORT);
        return repository.findAllByType(pageable, ArticleType.NORMAL).map(ArticleVO::new);
    }

    public ArticleVO viewArticle(int articleId) {
        Article article = repository.findByIdAndType(articleId, ArticleType.NORMAL);
        ToolKit.assertNotNull(articleId, article);
        return new ArticleVO(article);
    }

    public ArticleVO viewArticle(String alias) {
        Article article = repository.findByAliasAndType(alias, ArticleType.NORMAL);
        ToolKit.assertNotNull(alias, article);
        return new ArticleVO(article);
    }

    public ArticleVO viewPage(String alias) {
        Article article = repository.findByAliasAndType(alias, ArticleType.PAGE);
        ToolKit.assertNotNull(alias, article);
        return new ArticleVO(article);
    }

    public Page<ArticleVO> searchArticles(int page, int pageSize, String query) {
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query, "title", "summary", "content")
                .analyzer(ElasticsearchConfiguration.ANALYZER);
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").field("summary")
                .preTags("<b>").postTags("</b>");
        SearchResponse searchResponse = client.prepareSearch("lightning").setTypes("article")
                .setQuery(queryBuilder).highlighter(highlightBuilder)
                .setSearchType(SearchType.QUERY_THEN_FETCH).setFrom(page * pageSize).setSize(pageSize)
                .get();
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        Map<Integer, IndexedArticle> articles = new HashMap<>(hits.length);
        for(SearchHit hit : hits) {
            IndexedArticle article = new IndexedArticle();
            article.setId(Integer.valueOf(hit.getId()));
            HighlightField titleHighlight = hit.getHighlightFields().get("title");
            HighlightField summaryHighlight = hit.getHighlightFields().get("summary");
            article.setTitle(titleHighlight == null ? null : titleHighlight.fragments()[0].string());
            article.setSummary(summaryHighlight == null ? null : summaryHighlight.fragments()[0].string());
            articles.put(article.getId(), article);
        }
        List<ArticleVO> articleVOs = repository.findAllById(articles.keySet()).stream().map(ArticleVO::new).peek(articleVO -> {
            IndexedArticle article = articles.get(articleVO.getId());
            articleVO.setTitle(ToolKit.defaultIfNull(article.getTitle(), articleVO.getTitle()));
            articleVO.setSummary(ToolKit.defaultIfNull(article.getSummary(), articleVO.getSummary()));
        }).collect(Collectors.toList());
        return new PageImpl<>(articleVOs, PageRequest.of(page, pageSize), searchHits.getTotalHits());
    }

    @Transactional
    public ArticleVO updateArticle(int articleId, ArticleDTO articleDTO) {
        Article article = repository.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        BeanUtils.copyProperties(articleDTO, article);
        article.setModifiedAt(LocalDateTime.now());
        article = repository.save(article);
        searchRepository.index(article);
        return new ArticleVO(article);
    }

    @Transactional
    public void deleteArticle(int articleId) {
        Article article = repository.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        article.setDeletedAt(LocalDateTime.now());
        repository.save(article);
        searchRepository.deleteById(articleId);
    }
}
