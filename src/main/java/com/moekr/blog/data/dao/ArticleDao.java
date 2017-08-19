package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Article;
import com.moekr.blog.data.repository.ArticleRepository;
import com.moekr.blog.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleDao {
    private final ArticleRepository repository;

    @Autowired
    public ArticleDao(ArticleRepository repository) {
        this.repository = repository;
    }

    public Article save(Article article){
        return repository.save(article);
    }

    public List<Article> findAll(){
        return ToolKit.iterableToList(repository.findAll());
    }

    public Article findById(int articleId){
        return repository.findOne(articleId);
    }

    public Article delete(Article article){
        article.setDeletedAt(LocalDateTime.now());
        return repository.save(article);
    }
}
