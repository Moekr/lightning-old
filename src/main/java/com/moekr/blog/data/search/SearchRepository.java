package com.moekr.blog.data.search;

import com.moekr.blog.data.dto.ArticleDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<ArticleDTO, Integer> {
}
