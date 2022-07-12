package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DesignInfo {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String name;
    private String types;
    private double height;
    private double width;
    private double leftPosition;
    private double topPosition;
    private double rotate;
    private double scales;
    private String font;
    private String textColor;
    private String src;
    @ManyToOne
    BluePrint bluePrint;

}
