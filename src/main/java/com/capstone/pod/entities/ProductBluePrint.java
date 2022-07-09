package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBluePrint {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String frameImage;
    private String position;
    private double placeHolderTop;
    private double placeHolderHeight;
    private double placeHolderWidth;
    private double widthRate;
    private double heightRate;

    @ManyToOne
    Product product;
}
