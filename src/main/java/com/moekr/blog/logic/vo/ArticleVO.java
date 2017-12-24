package com.moekr.blog.logic.vo;

import com.moekr.blog.data.entity.Article;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ArticleVO implements Serializable {
    private int id;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int views;
    private CategoryVO category;
    private List<TagVO> tags;

    public ArticleVO(Article article){
        BeanUtils.copyProperties(article,this, "category", "tags");
        this.category = new CategoryVO(article.getCategory());
        this.tags = article.getTags().stream().map(TagVO::new).collect(Collectors.toList());
    }
}
