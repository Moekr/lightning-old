package com.moekr.blog.logic.vo;

import com.moekr.blog.data.entity.Article;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ArticleVo {
    private int id;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int views;
    private CategoryVo category;
    private List<TagVo> tags;

    public ArticleVo(Article article){
        BeanUtils.copyProperties(article,this, "category", "tags");
        this.category = new CategoryVo(article.getCategory());
        this.tags = article.getTags().stream().map(TagVo::new).collect(Collectors.toList());
    }
}
