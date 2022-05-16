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
public class DesignedProduct {
    @GeneratedValue
    @Id
    private int id;
    private LocalDate createDate;
    private String name;
    private double designedPrice;
    private String image;

    @OneToMany(mappedBy = "designedProduct")
    Collection<DesignBluePrint> designBluePrints;

    @ManyToOne
    Product product;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<Rating> ratings;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Collection<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Collection<DesignedProductTag> designedProductTags;


}
