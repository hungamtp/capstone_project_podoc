package com.capstone.pod.dto.category;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryDto {
    @NotBlank(message = ValidationMessage.CATEGORY_NAME_VALID_MESSAGE)
    private String name;
    private String image;
    private boolean isDeleted;
}
