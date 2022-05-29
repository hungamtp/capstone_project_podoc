package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SizeColorByFactory {
    @Id
    @GeneratedValue
    private int id;
    private int quantity;

    @ManyToOne
    Factory factory;

    @ManyToOne
    SizeColor sizeColor;
}
