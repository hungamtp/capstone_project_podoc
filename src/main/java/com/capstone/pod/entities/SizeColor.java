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
public class SizeColor {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Color color;

    @ManyToOne
    private Size size;

    @ManyToOne
    private Product product;

    @OneToMany(mappedBy = "sizeColor")
    private Collection<SizeColorByFactory> sizeColorByFactories;

}
