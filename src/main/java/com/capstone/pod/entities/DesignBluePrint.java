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
public class DesignBluePrint {
    @GeneratedValue
    @Id
    private int id;

    @ManyToOne
    BluePrint bluePrint;

    @ManyToOne
    DesignedProduct designedProduct;

}
