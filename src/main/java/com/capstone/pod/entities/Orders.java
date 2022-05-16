package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue
    private int id;
    private LocalDate orderDate;
    private double price;
    private String address;
    private String phone;
    private String customerName;
    @ManyToOne
    User user;
    @ManyToOne
    OrderStatus orderStatus;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    Collection<OrderDetail> orderDetails;
}
