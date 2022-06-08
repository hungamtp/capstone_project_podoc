package com.capstone.pod.dto.blueprint;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.designinfo.DesignInfoDto;
import com.capstone.pod.dto.placeholder.PlaceHolderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class BluePrintDto {
    @NotBlank(message = ValidationMessage.FRAME_IMAGE_VALID_MESSAGE)
    private String frameImage;
    @NotBlank(message = ValidationMessage.POSITION_VALID_MESSAGE)
    private String position;
    @NotNull(message = ValidationMessage.POSITION_VALID_MESSAGE)
    private PlaceHolderDto placeholder;
    @NotNull(message = ValidationMessage.DESIGN_INFOS_VALID_MESSAGE)
    private List<DesignInfoDto> designInfoDtos;
}
