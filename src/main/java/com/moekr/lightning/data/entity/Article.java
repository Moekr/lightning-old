package com.moekr.lightning.data.entity;

import com.moekr.lightning.data.converter.StringSetConverter;
import com.moekr.lightning.util.ArticleType;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "ENTITY_ARTICLE", indexes = @Index(columnList = "alias"))
@Where(clause = "deleted_at IS NULL")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "alias")
    private String alias;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "summary")
    private String summary;

    @Basic
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Basic
    @Column(name = "tags", columnDefinition = "JSON NOT NULL")
    @Convert(converter = StringSetConverter.class)
    private Set<String> tags;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ArticleType type = ArticleType.NORMAL;

    @Basic
    @Column(name = "views")
    private Integer views = 0;

    @Basic
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Basic
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Basic
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
