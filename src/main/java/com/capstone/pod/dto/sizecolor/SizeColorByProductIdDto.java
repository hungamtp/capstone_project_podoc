package com.capstone.pod.dto.sizecolor;

import com.capstone.pod.dto.color.ColorInDesignDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeColorByProductIdDto {
    private List<String> sizes;
    private List<ColorInDesignDto> colors;
}
