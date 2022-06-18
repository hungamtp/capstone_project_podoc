package com.capstone.pod.dto.cartdetail;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddToCartDTO {
    private Integer designId;
    private String size;
    private String color;
    private Integer quantity;
}
