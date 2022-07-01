package com.capstone.pod.dto.size;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeDto {
    private String id;
    private String name;
}
