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
public class BluePrint {
    @Id
    @GeneratedValue
    private int id;
    private String frameImage;
    private String position;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    Placeholder placeholder;

    @ManyToOne
    Product product;

    @OneToMany(mappedBy = "bluePrint")
    Collection<DesignBluePrint> designBluePrints;
}
