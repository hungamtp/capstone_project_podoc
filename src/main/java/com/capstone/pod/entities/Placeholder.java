package com.capstone.pod.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "placeholder")
public class Placeholder {
    @Id
    @GeneratedValue
    private int id;
    private double topCoordinate;
    private double leftCoordinate;
    private double width;
    private double height;

    @OneToOne
    BluePrint bluePrint;
}
