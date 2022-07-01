package com.capstone.pod.entities;


import com.capstone.pod.dto.support.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountTime extends Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isExpired;

    @ManyToOne
    Discount discount;
}
