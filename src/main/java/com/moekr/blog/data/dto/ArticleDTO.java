package com.moekr.blog.data.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "blog", type = "article")
public class ArticleDTO {
    private int id;
    private String title;
    private String summary;
    private String content;
}
