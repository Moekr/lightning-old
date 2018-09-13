package com.moekr.lightning.data.repository;

import com.moekr.lightning.data.entity.Article;
import com.moekr.lightning.util.ArticleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    default Article findById(int id) {
        return this.findById(Integer.valueOf(id)).orElse(null);
    }

    Page<Article> findAllByType(Pageable pageable, ArticleType type);

    Article findByIdAndType(Integer id, ArticleType type);

    Article findByAliasAndType(String alias, ArticleType type);
}
