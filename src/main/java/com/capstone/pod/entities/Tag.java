package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
public class Tag {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String name;
    private boolean isExist;

    @OneToMany(mappedBy = "tag")
    private Collection<DesignedProductTag> designedProductTags;

    @OneToMany(mappedBy = "tag")
    private Collection<ProductTag> productTags;
}
