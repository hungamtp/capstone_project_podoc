package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String name;
    private float discountPercent;

    @OneToMany(mappedBy = "discount")
    private Collection<PriceByFactory> priceByFactories;

    @OneToMany(mappedBy = "discount")
    private Collection<DiscountTime> discountTimes;
}
