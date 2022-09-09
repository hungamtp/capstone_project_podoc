package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
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

    @OneToMany(mappedBy = "follower")
    private List<Follower> followers ;

    @OneToMany(mappedBy = "user")
    private List<Follower> idol ;
}
