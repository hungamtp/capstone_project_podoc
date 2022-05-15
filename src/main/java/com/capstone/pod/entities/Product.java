package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private double price;
    private boolean isPublic;
    private LocalDate createDate;
    private LocalDate  modifiedDate;
    private String description;
}
