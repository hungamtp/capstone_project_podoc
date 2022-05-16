package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfo {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private String shippingAddress;

    @ManyToOne
    User user;
}
