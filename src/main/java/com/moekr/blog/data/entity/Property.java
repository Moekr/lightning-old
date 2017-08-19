package com.moekr.blog.data.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ENTITY_PROPERTY")
public class Property {
    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "value")
    private String value;
}
