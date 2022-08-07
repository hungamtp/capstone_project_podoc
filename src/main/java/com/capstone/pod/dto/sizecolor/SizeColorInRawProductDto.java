package com.capstone.pod.dto.sizecolor;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeColorInRawProductDto {
    private String color;
    private List<String> sizes;
}
