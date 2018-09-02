package com.moekr.lightning.data.repository;

import com.moekr.lightning.data.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Article findByAlias(String alias);
}
