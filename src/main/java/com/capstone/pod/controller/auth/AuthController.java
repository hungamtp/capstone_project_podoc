package com.capstone.pod.controller.auth;

import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Validated @RequestBody LoginDto user) {
        ResponseDto<LoginResponseDto> responseDTO = new ResponseDto();
        LoginResponseDto loginResponseDTO = null;
        loginResponseDTO = userService.login(user);
        responseDTO.setData(loginResponseDTO);
        responseDTO.setSuccessMessage("LOGIN_SUCCESS");
        return ResponseEntity.ok().body(responseDTO);
    }

}
