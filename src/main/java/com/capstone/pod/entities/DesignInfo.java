package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.*;

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
    private String font;
    private String textColor;
    private String src;
    @ManyToOne
    BluePrint bluePrint;

}
