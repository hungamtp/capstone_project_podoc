package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBluePrint {
    @Id
    @GeneratedValue
    private int id;
    private String frameImage;
    private String position;
    private double placeHolderHeight;
    private double placeHolderWidth;

    @ManyToOne
    Product product;
}
