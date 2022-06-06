package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Factory{
    @Id
    @GeneratedValue
    private int id;
    private boolean isCollaborating;

    private String name;

    private String location;

    @OneToOne(mappedBy = "factory")
    private Credential credential;

    @OneToMany(mappedBy = "factory")
    private Collection<SizeColorByFactory> sizeColorByFactories;

    @OneToMany(mappedBy = "factory")
    private Collection<PriceByFactory> priceByFactories;

    @OneToMany(mappedBy = "factory",cascade = CascadeType.ALL)
    private Collection<Orders> orders;
}
