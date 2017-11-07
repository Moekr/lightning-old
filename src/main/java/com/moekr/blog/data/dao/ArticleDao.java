package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Article;
import com.moekr.blog.data.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ArticleDao extends AbstractDao<Article, Integer>{
    private final ArticleRepository repository;

    @Autowired
    public ArticleDao(ArticleRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public void delete(Article article){
        article.setDeletedAt(LocalDateTime.now());
        repository.save(article);
    }
}
