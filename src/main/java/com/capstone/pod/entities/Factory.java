package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Factory{
    @Id
    @GeneratedValue
    private int id;
    private boolean isCollaborating;

    @OneToOne(mappedBy = "factory")
    private Credential credential;

    @OneToMany(mappedBy = "factory")
    private Collection<SizeColorByFactory> sizeColorByFactories;

    @OneToMany(mappedBy = "factory")
    private Collection<PriceByFactory> priceByFactories;

    @OneToMany(mappedBy = "factory",cascade = CascadeType.ALL)
    private Collection<Orders> orders;
}
