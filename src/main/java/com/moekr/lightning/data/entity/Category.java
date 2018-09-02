package com.moekr.lightning.data.entity;

import com.moekr.lightning.util.Visible;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ENTITY_CATEGORY")
public class Category implements Visible {
    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "level", columnDefinition = "INT(11) DEFAULT 50")
    private int level = 50;

    @Basic
    @Column(name = "visible", columnDefinition = "BIT DEFAULT 1")
    private boolean visible = true;

    @OneToMany(targetEntity = Article.class, mappedBy = "category")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Article> articles;
}
