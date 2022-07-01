package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String name;
    private String description;
    private double designedPrice;
    private boolean publish;


    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<BluePrint> bluePrints;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    List<DesignColor> designColors;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    Collection<ImagePreview> imagePreviews;

    @ManyToOne
    PriceByFactory priceByFactory;

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
