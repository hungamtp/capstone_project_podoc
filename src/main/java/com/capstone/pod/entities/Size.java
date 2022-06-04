package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
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
public class Size {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany(mappedBy = "size")
    private Collection<SizeColor> sizeColors;
}
