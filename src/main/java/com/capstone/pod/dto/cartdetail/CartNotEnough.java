package com.capstone.pod.dto.cartdetail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartNotEnough {
    private Integer id;
    private Integer quantityAvailable;
}
