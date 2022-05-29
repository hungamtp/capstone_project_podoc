package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue
    private int id;
    private String comment;
    private float ratingStar;
    private LocalDate ratingDate;

    @ManyToOne
    DesignedProduct designedProduct;
    @ManyToOne
    User user;
}
