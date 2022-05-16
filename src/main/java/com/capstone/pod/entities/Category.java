package com.capstone.pod.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String image;
    private boolean isDeleted;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private Collection<Product> products;

}
