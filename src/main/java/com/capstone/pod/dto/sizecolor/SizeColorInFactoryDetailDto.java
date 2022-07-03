package com.capstone.pod.dto.sizecolor;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SizeColorInFactoryDetailDto {
    private int quantity;
    private String size;
    private String colorImage;
}
