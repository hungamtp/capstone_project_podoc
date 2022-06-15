package com.capstone.pod.controller.factory;

import com.capstone.pod.constant.factory.FactorySuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.constant.user.UserSuccessMessage;
import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.factory.FactoryPageResponseDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.user.AddUserDto;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.services.FactoryService;
import com.capstone.pod.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("factory")
@RequiredArgsConstructor
public class FactoryController {
    private final FactoryService factoryService;

    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<ResponseDto> getAll(@RequestParam int pageNumber, @RequestParam int pageSize)
    {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        ResponseDto<Page<FactoryPageResponseDto>> responseDTO = new ResponseDto();
        Page<FactoryPageResponseDto> getAllFactory = factoryService.getAllFactories(pageable);
        responseDTO.setData(getAllFactory);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_ALL_FACTORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<ResponseDto> add(@Validated @RequestBody AddFactoryDto factoryDto)
    {
        ResponseDto<AddFactoryResponse> responseDTO = new ResponseDto();
        AddFactoryResponse addFactory = factoryService.addFactory(factoryDto);
        responseDTO.setData(addFactory);
        responseDTO.setSuccessMessage(UserSuccessMessage.ADD_FATORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER)
    @PostMapping("avatar/{id}")
    public ResponseEntity<ResponseDto> updateAvatar(@Validated @RequestBody UpdateAvatarDto avatar, @PathVariable(name = "id") int id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = factoryService.updateAvatar(avatar, id);
        responseDTO.setSuccessMessage(FactorySuccessMessage.UPDATE_FACTORY_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping("password-change/{id}")
    public ResponseEntity<ResponseDto> updatePassword(@Validated @RequestBody UpdatePasswordDto user, @PathVariable(name = "id") int id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = factoryService.updatePassword(user, id);
        responseDTO.setSuccessMessage(FactorySuccessMessage.UPDATE_FACTORY_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
}