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
public class SizeColor {
    @Id
    @GeneratedValue
    private int id;
    private int quantity;

    @ManyToOne
    private Color color;

    @ManyToOne
    private Size size;

    @ManyToOne
    private Product product;

    @OneToMany(mappedBy = "sizeColor")
    private Collection<SizeColorByFactory> sizeColorByFactories;

}
