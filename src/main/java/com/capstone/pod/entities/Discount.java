package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private float discountPercent;

    @OneToMany(mappedBy = "discount")
    private Collection<PriceByFactory> priceByFactories;

    @OneToMany(mappedBy = "discount")
    private Collection<DiscountTime> discountTimes;
}
