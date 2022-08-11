package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderStateDto {
    private String name;
    private LocalDateTime createDate;
}
