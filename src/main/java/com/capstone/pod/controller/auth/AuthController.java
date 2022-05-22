package com.capstone.pod.controller.auth;

import com.capstone.pod.constant.auth.AuthSuccessMessage;
import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.user.RegisterUserDto;
import com.capstone.pod.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Validated @RequestBody LoginDto user) {
        ResponseDto<LoginResponseDto> responseDTO = new ResponseDto();
        LoginResponseDto loginResponseDTO = userService.login(user);
        responseDTO.setData(loginResponseDTO);
        responseDTO.setSuccessMessage(AuthSuccessMessage.LOGIN_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Validated @RequestBody RegisterUserDto user) throws RoleNotFoundException {
        ResponseDto<RegisterResponseDto> responseDTO = new ResponseDto();
        RegisterResponseDto registerResponseDto = userService.register(user);
        responseDTO.setData(registerResponseDto);
        responseDTO.setSuccessMessage(AuthSuccessMessage.LOGIN_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
