package com.jimine.jiminebackend.model;

import jakarta.persistence.*;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
}
