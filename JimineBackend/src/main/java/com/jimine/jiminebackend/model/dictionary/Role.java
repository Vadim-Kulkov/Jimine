package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "role")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "role_id")),
        @AttributeOverride(name = "name", column = @Column(name = "role_name"))
})
public class Role extends BaseDictionary {

}
