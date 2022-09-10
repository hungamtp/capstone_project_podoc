package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TradeDiscount {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private float tradeDiscount;
    private LocalDate start;
    private LocalDate end;

    @ManyToOne
    private Factory factory;
}
