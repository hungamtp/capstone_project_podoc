package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Placeholder {
    @Id
    @GeneratedValue
    private int id;
    private double top;
    private double left;
    private double width;
    private double height;

    @OneToOne(mappedBy = "placeholder")
    BluePrint bluePrint;
}
