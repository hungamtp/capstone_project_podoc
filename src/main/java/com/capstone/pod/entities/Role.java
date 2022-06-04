package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
    },
    indexes = {
        @Index(columnList = "name")
    }
)
public class Role {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany(mappedBy= "role")
    Collection<Credential> credentials;
}
