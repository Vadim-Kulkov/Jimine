package com.jimine.jiminebackend.model.dictionary;

import com.jimine.jiminebackend.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseDictionary extends BaseEntity {

    @Column(name = "name")
    protected String name;
}
