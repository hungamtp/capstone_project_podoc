package com.capstone.pod.dto.designinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DesignInfoDto {
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
}
