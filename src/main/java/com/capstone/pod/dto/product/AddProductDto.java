package com.capstone.pod.dto.product;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
public class AddProductDto {
    @NotBlank(message = ValidationMessage.PRODUCT_NAME_VALID_MESSAGE)
    private String name;
    @NotBlank(message = ValidationMessage.PRODUCT_DESCRIPTION_VALID_MESSAGE)
    @Size(min = ValidationSize.PRODUCT_DESCRIPTION_MIN,message = ValidationMessage.PRODUCT_DESCRIPTION_SIZE_VALID_MESSAGE)
    private String description;
    @NotBlank(message = ValidationMessage.PRODUCT_IMAGES_VALID_MESSAGE)
    private List<String> images;
    @NotBlank(message = ValidationMessage.PRODUCT_CATEGORY_VALID_MESSAGE)
    private String categoryName;
}
