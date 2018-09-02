package com.moekr.lightning.data.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ENTITY_REDIRECTION")
public class Redirection {
    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "location", length = 2048)
    private String location;

    @Basic
    @Column(name = "views")
    private int views;
}
