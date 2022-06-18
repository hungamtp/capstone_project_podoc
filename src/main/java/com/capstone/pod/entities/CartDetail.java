package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetail {
    @Id
    @GeneratedValue
    private Integer id;
    private int quantity;
    private String color;
    private String size;
    @ManyToOne
    @JoinColumn(name = "designed_product_id")
    private DesignedProduct designedProduct;
    @ManyToOne
    private Cart cart;
}
