package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User{
    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;
    private String status;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<Rating> ratings;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<ShippingInfo> shippingInfos;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<Orders> orders;
    @OneToOne(mappedBy = "user")
    Credential credential;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<DesignedProduct> designedProducts;
}
