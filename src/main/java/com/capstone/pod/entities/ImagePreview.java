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
public class ImagePreview {
    @GeneratedValue
    @Id
    private int id;
    private String image;
    private String color;
    private String position;
    @ManyToOne
    DesignedProduct designedProduct;
}
