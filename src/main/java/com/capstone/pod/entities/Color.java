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
public class Color {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String imageColor;
    @OneToMany(mappedBy = "color")
    private Collection<SizeColor> sizeColors;
    @OneToMany(mappedBy = "color")
    private Collection<DesignColor> designColors;

}
