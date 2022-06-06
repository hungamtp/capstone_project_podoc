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
public class BluePrint {
    @Id
    @GeneratedValue
    private int id;
    private String frameImage;
    private String position;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    Placeholder placeholder;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    DesignInfo designInfo;

    @ManyToOne
    Product product;

    @OneToMany(mappedBy = "bluePrint")
    Collection<DesignBluePrint> designBluePrints;
}
