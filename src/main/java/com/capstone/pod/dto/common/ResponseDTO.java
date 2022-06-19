package com.capstone.pod.dto.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {
    private Object data;
    private String errorMessage;
}
