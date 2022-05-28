package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
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
public class Product extends Auditable {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private double price;
    private boolean isPublic;
    private String description;
    private boolean isDeleted;

    @OneToMany(mappedBy = "product")
    private Collection<BluePrint> bluePrints;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<DesignedProduct> designedProducts;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<ProductImages> productImages;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<SizeColor> sizeColors;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<ProductTag> productTags;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<PriceByFactory> priceByFactories;


    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

}
