package com.moekr.blog.data.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "ENTITY_ARTICLE")
@Where(clause = "deleted_at IS NULL")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Basic
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Basic
    @Column(name = "deleted_At")
    private LocalDateTime deletedAt;

    @Basic
    @Column(name = "views")
    private int views;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;

    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(name = "LINK_ARTICLE_TAG",
            joinColumns = @JoinColumn(name = "article", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag", referencedColumnName = "id")
    )
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Tag> tags;
}
