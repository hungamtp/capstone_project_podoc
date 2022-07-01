package com.capstone.pod.dto.color;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ColorInDesignDto {
    private String id;
    private String name;
    private String image;
}
