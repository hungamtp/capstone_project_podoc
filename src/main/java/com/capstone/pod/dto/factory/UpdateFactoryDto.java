package com.capstone.pod.dto.factory;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateFactoryDto {
    @NotBlank(message = ValidationMessage.NAME_VALID_MESSAGE)
    private String name;
    @NotNull(message = ValidationMessage.TRADE_DISCOUNT_VALID_MESSAGE)
    private double tradeDiscount;
    @NotBlank(message = ValidationMessage.ADDRESS_VALID_MESSAGE)
    @Size(min = ValidationSize.ADDRESS_MIN,message = ValidationMessage.ADDRESS_SIZE_VALID_MESSAGE)
    private String address;
    @Size(min = ValidationSize.PHONE_MIN,max = ValidationSize.PHONE_MAX,message = ValidationMessage.PHONE_SIZE_VALID_MESSAGE)
    @NotBlank(message = ValidationMessage.PHONE_NOT_EMPTY_VALID_MESSAGE)
    private String phone;
}
