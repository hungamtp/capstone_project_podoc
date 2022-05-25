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
public class Role {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany(mappedBy= "role")
    Collection<Credential> credentials;

}
