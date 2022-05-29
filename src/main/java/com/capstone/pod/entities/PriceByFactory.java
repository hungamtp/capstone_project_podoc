package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceByFactory {
    @Id
    @GeneratedValue
    private int id;
    private double price;
    @ManyToOne
    private Factory factory;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Discount discount;
}
