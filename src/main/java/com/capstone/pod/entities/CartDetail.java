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
public class CartDetail {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private int quantity;
    private String color;
    private String size;
    @ManyToOne
    @JoinColumn(name = "designed_product_id")
    private DesignedProduct designedProduct;
    @ManyToOne
    private Cart cart;
}
