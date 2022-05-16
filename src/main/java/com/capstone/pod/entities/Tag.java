package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private boolean isExist;

    @OneToMany(mappedBy = "tag")
    private Collection<DesignedProductTag> designedProductTags;

    @OneToMany(mappedBy = "tag")
    private Collection<ProductTag> productTags;
}
