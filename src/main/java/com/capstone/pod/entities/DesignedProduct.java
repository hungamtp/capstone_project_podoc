package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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
    private String description;
    private double designedPrice;
    private boolean publish;


    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<BluePrint> bluePrints;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<DesignColor> designColors;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<ImagePreview> imagePreviews;

    @ManyToOne
    Product product;
    @ManyToOne
    User user;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<Rating> ratings;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Collection<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Collection<DesignedProductTag> designedProductTags;


}
