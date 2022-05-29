package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Setter
@Getter
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
