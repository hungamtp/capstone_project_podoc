package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String phone;
    private String email;
    private boolean isMailVerified;
    private boolean isActive;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<Rating> ratings;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<ShippingInfo> shippingInfos;

    @ManyToOne(cascade = CascadeType.ALL)
    Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<Orders> orders;

}
