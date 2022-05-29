package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTag {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Product product;
    @ManyToOne
    private Tag tag;
}
