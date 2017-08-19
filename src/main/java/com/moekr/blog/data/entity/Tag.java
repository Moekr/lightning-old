package com.moekr.blog.data.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ENTITY_TAG")
public class Tag {
    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToMany(targetEntity = Article.class)
    @JoinTable(name = "LINK_ARTICLE_TAG",
            joinColumns = @JoinColumn(name = "tag", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "article", referencedColumnName = "id")
    )
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Article> articles;
}
