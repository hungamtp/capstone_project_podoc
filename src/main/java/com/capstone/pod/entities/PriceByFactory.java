package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceByFactory {
    @Id @GeneratedValue
    private int id;
    private double price;
    @ManyToOne
    private Factory factory;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Discount discount;
    @OneToMany(mappedBy = "priceByFactory")
    private List<DesignedProduct> designedProducts;
}
