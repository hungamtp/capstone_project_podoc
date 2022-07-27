package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceByFactory {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private double price;
    private String material;
    @ManyToOne
    private Factory factory;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Discount discount;
    @OneToMany(mappedBy = "priceByFactory")
    private List<DesignedProduct> designedProducts;
}
