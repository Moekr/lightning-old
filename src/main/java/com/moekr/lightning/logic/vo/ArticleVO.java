package com.moekr.lightning.logic.vo;

import com.moekr.lightning.data.entity.Article;
import com.moekr.lightning.util.ArticleType;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ArticleVO implements Serializable {
    private int id;
    private String alias;
    private String title;
    private String summary;
    private String content;
    private Set<String> tags;
    private ArticleType type;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleVO(Article article) {
        BeanUtils.copyProperties(article, this);
    }

    public boolean isNormal() {
        return type == ArticleType.NORMAL;
    }
}
