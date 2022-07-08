package com.capstone.pod.dto.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto {
    private Object data;
    private String errorMessage;
}
