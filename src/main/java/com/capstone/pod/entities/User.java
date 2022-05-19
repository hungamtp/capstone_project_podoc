package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable {
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
    private String avatar;
    private boolean isMailVerified;
    private boolean isActive;
    private String status;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<Rating> ratings;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<ShippingInfo> shippingInfos;
    @ManyToOne(cascade = CascadeType.ALL)
    Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    Collection<Orders> orders;

}
