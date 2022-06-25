package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue
    private int id;
    private int quantity;
    private String color;
    private String size;
    @ManyToOne
    private Orders orders;
    @ManyToOne
    private DesignedProduct designedProduct;
    @ManyToOne
    private Factory factory;
    @OneToMany(mappedBy = "orderDetail")
    List<OrderStatus> orderStatuses;
}
