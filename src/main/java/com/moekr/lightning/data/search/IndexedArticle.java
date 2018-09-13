package com.moekr.lightning.data.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "lightning", type = "article")
public class IndexedArticle {
    @Id
    @Field(type = FieldType.Integer, index = false, store = true)
    private int id;
    @Field(type = FieldType.Text, analyzer = ElasticsearchConfiguration.ANALYZER, store = true)
    private String title;
    @Field(type = FieldType.Text, analyzer = ElasticsearchConfiguration.ANALYZER, store = true)
    private String summary;
    @Field(type = FieldType.Text, analyzer = ElasticsearchConfiguration.ANALYZER, store = true)
    private String content;
}
