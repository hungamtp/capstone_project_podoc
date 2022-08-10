package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Factory{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private boolean isCollaborating;
    private String name;
    private double tradeDiscount;
    private String location;

    @OneToOne(mappedBy = "factory")
    private Credential credential;

    @OneToMany(mappedBy = "factory")
    private List<SizeColorByFactory> sizeColorByFactories;

    @OneToMany(mappedBy = "factory")
    private Collection<PriceByFactory> priceByFactories;

    @OneToMany(mappedBy = "factory",cascade = CascadeType.ALL)
    private Collection<OrderDetail> orderDetails;
}
