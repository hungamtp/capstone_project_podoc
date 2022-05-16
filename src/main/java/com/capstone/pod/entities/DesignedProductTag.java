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
public class DesignedProductTag {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private DesignedProduct designedProduct;
    @ManyToOne
    private Tag tag;
}
