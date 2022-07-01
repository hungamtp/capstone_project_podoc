package com.capstone.pod.dto.cartdetail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartNotEnoughDto {
    private String id;
    private Integer quantityAvailable;
}
