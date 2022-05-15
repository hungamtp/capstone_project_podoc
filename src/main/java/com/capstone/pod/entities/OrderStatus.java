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
public class OrderStatus {
    @Id
    @GeneratedValue
    private int id;
    private String statusName;
    private LocalDate updateStatusDate;
}
