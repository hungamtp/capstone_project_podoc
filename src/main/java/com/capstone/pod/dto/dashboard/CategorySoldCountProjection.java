package com.capstone.pod.dto.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CategorySoldCountProjection {
    private String id;
    private String name;
    private String image;
    private Long count;
}
