package com.moekr.blog.data.dao;

import com.moekr.blog.data.dto.ArticleDTO;
import com.moekr.blog.data.entity.Article;
import com.moekr.blog.data.repository.ArticleRepository;
import com.moekr.blog.data.search.ElasticsearchConfiguration;
import com.moekr.blog.data.search.SearchRepository;
import com.moekr.blog.util.ToolKit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleDAO extends AbstractDAO<Article, Integer> {
    private final ArticleRepository repository;
    private final SearchRepository searchRepository;

    @Autowired
    public ArticleDAO(ArticleRepository repository, SearchRepository searchRepository) {
        super(repository);
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @PostConstruct
    private void index() {
        findAll().stream().map(a -> ToolKit.copyProperties(a, ArticleDTO::new)).forEach(searchRepository::index);
    }

    public List<Article> search(String key) {
        QueryBuilder queryBuilder = new QueryStringQueryBuilder(key).analyzer(ElasticsearchConfiguration.ANALYZER);
        return repository.findAll(ToolKit.iterableToList(searchRepository.search(queryBuilder)).stream().map(ArticleDTO::getId).collect(Collectors.toList()));
    }

    @Override
    public Article save(Article entity) {
        Article article = super.save(entity);
        searchRepository.index(ToolKit.copyProperties(article, ArticleDTO::new));
        return article;
    }

    public Article findByAlias(String alias) {
        return repository.findByAlias(alias);
    }

    @Override
    public void delete(Article article) {
        article.setDeletedAt(LocalDateTime.now());
        repository.save(article);
        searchRepository.delete(article.getId());
    }
}
