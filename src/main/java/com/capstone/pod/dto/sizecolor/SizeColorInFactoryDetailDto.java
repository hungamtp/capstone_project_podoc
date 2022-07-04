package com.capstone.pod.dto.sizecolor;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SizeColorInFactoryDetailDto {
    int quantity;
    String size;
    String colorImage;
}
