package com.moekr.lightning.data.search;

import com.moekr.lightning.data.dto.ArticleDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<ArticleDTO, Integer> {
}
