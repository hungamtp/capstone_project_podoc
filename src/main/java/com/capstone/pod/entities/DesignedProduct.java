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
public class DesignedProduct  extends Auditable {
    @GeneratedValue
    @Id
    private int id;
    private String name;
    private double designedPrice;
    private String image;

    private double price;

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
