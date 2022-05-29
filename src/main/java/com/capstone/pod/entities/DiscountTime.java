package com.capstone.pod.entities;


import com.capstone.pod.dto.support.Auditable;
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
public class DiscountTime extends Auditable {
    @Id
    @GeneratedValue
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isExpired;

    @ManyToOne
    Discount discount;
}
