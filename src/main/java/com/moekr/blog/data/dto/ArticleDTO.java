package com.moekr.blog.data.dto;

import com.moekr.blog.data.search.ElasticsearchConfiguration;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "blog", type = "article")
public class ArticleDTO {
    @Id
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private int id;
    @Field(type = FieldType.String, analyzer = ElasticsearchConfiguration.ANALYZER, store = true)
    private String title;
    @Field(type = FieldType.String, analyzer = ElasticsearchConfiguration.ANALYZER, store = true)
    private String summary;
    @Field(type = FieldType.String, analyzer = ElasticsearchConfiguration.ANALYZER, store = true)
    private String content;
}
