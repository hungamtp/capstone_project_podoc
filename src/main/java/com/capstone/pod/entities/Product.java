package com.capstone.pod.entities;

import com.capstone.pod.dto.support.AuditableDateTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
    },
    indexes = {
        @Index(columnList = "name , isDeleted , isPublic")
    }
)
public class Product extends AuditableDateTime {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
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
    private List<SizeColor> sizeColors;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<ProductTag> productTags;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Collection<PriceByFactory> priceByFactories;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<SizeProduct> sizeProduct;

}
