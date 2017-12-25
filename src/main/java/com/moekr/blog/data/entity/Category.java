package com.moekr.blog.data.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ENTITY_CATEGORY")
public class Category {
    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = Article.class, mappedBy = "category")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Article> articles;
}
