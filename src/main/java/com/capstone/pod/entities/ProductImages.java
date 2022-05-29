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
public class ProductImages {
    @GeneratedValue
    @Id
    private int id;
    private String image;

    @ManyToOne
    private Product product;
}
