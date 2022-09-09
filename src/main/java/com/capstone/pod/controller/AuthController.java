package com.capstone.pod.controller;

import com.capstone.pod.constant.auth.AuthSuccessMessage;
import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.auth.RegisterDto;
import com.capstone.pod.services.AuthService;
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
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Validated @RequestBody LoginDto user) {
        ResponseDto<LoginResponseDto> responseDTO = new ResponseDto();
        LoginResponseDto loginResponseDTO = authService.login(user);
        responseDTO.setData(loginResponseDTO);
        responseDTO.setSuccessMessage(AuthSuccessMessage.LOGIN_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Validated @RequestBody RegisterDto user) throws RoleNotFoundException {
        ResponseDto<RegisterResponseDto> responseDTO = new ResponseDto();
        RegisterResponseDto registerResponseDto = authService.register(user);
        responseDTO.setData(registerResponseDto);
        responseDTO.setSuccessMessage(AuthSuccessMessage.REGISTER_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
