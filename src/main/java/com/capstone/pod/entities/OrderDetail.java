package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private int quantity;
    private String color;
    private String size;
    private boolean isRate;
    @ManyToOne
    private Orders orders;
    @ManyToOne
    private DesignedProduct designedProduct;
    @ManyToOne
    private Factory factory;
    @OneToMany(mappedBy = "orderDetail",cascade = CascadeType.ALL)
    List<OrderStatus> orderStatuses;
}
