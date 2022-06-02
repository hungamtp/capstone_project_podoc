package com.capstone.pod.dto.commom;

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
