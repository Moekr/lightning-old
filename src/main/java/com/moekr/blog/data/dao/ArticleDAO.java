package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Article;
import com.moekr.blog.data.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ArticleDAO extends AbstractDAO<Article, Integer> {
    private final ArticleRepository repository;

    @Autowired
    public ArticleDAO(ArticleRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void delete(Article article) {
        article.setDeletedAt(LocalDateTime.now());
        repository.save(article);
    }
}
