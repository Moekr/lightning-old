package com.moekr.blog.data.repository;

import com.moekr.blog.data.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Article findByAlias(String alias);
}
