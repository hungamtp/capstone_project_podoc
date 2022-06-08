package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
    },
    indexes = {
        @Index(columnList = "name , isDeleted , isPublic")
    }
)
public class Product extends Auditable {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private boolean isDeleted;
    private boolean isPublic;

    @OneToMany(mappedBy = "product")
    private Collection<ProductBluePrint> productBluePrints;

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
