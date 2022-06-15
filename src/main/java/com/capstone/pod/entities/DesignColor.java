package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DesignColor {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Color color;
    @ManyToOne
    private DesignedProduct designedProduct;
}
