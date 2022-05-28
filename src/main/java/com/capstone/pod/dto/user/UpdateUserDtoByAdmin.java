package com.capstone.pod.dto.user;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Data
public class UpdateUserDtoByAdmin {
    @NotBlank(message = ValidationMessage.FIRSTNAME_VALID_MESSAGE)
    private String firstName;
    @NotBlank(message = ValidationMessage.LASTNAME_VALID_MESSAGE)
    private String lastName;
    @NotBlank(message = ValidationMessage.ADDRESS_VALID_MESSAGE)
    @Size(min = ValidationSize.ADDRESS_MIN,message = ValidationMessage.ADDRESS_SIZE_VALID_MESSAGE)
    private String address;
    @Size(min = ValidationSize.PHONE_MIN,max = ValidationSize.PHONE_MAX,message = ValidationMessage.PHONE_SIZE_VALID_MESSAGE)
    @NotBlank(message = ValidationMessage.PHONE_NOT_EMPTY_VALID_MESSAGE)
    private String phone;
    @NotBlank(message = ValidationMessage.STATUS_VALID_MESSAGE)
    private String status;
}
