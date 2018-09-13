package com.moekr.lightning.data.search;

import com.moekr.lightning.data.entity.Article;
import com.moekr.lightning.util.ArticleType;
import com.moekr.lightning.util.ToolKit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<IndexedArticle, Integer> {
    default void index(Article article) {
        if (article.getType() == ArticleType.NORMAL) {
            this.index(ToolKit.copyProperties(article, IndexedArticle::new));
        } else {
            this.deleteById(article.getId());
        }
    }
}
