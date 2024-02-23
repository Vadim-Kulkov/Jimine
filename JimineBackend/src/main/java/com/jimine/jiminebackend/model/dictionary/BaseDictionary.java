package com.jimine.jiminebackend.model.dictionary;

import com.jimine.jiminebackend.model.BaseEntity;
import jakarta.persistence.*;

@MappedSuperclass
public class BaseDictionary extends BaseEntity {

    @Column(name = "name")
    protected String name;
}
