package com.capstone.pod.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImages {
    @GeneratedValue
    @Id
    private int id;
    private String image;
}
