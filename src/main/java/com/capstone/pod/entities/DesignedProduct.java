package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    List<BluePrint> bluePrints;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    List<DesignColor> designColors;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    List<ImagePreview> imagePreviews;

    @ManyToOne
    PriceByFactory priceByFactory;

    @ManyToOne
    Product product;
    @ManyToOne
    User user;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Set<Rating> ratings;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "designedProduct",cascade = CascadeType.ALL)
    private Set<DesignedProductTag> designedProductTags;


}
