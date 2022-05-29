package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends Auditable {
    @Id
    @GeneratedValue
    private int id;
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

    @ManyToOne
    private Factory factory;
}
