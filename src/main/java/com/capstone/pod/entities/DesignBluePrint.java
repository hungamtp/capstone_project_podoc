package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Data
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
