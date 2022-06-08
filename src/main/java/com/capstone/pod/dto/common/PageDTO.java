package com.capstone.pod.dto.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDTO {
    private Object data;
    private int page;
    private int elements;
}
