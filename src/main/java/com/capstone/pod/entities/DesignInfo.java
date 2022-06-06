package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DesignInfo {
    @GeneratedValue
    @Id
    private int id;
    private String name;
    private String types;
    private double height;
    private double width;
    private double leftPosition;
    private double topPosition;
    private double x;
    private double y;
    private double rotate;
    private double scales;
    private String src;
    @OneToOne
    BluePrint bluePrint;

}
