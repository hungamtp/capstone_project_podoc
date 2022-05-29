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
