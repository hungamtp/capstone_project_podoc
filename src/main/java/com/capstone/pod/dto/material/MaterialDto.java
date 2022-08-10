package com.capstone.pod.dto.material;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class MaterialDto {
    private String id;

    @NotBlank(message = "Material can not be blank")
    @Min(value = 5,message = "Material must have at least 5 characters")
    private String name;
}
