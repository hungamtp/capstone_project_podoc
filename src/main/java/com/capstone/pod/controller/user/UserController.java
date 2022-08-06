package com.capstone.pod.controller.user;

import com.capstone.pod.constant.factory.FactorySuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.constant.user.UserSuccessMessage;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.user.*;
import com.capstone.pod.services.EmailService;
import com.capstone.pod.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    @GetMapping("/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<ResponseDto> getUserById(@PathVariable(name = "id") String userId)  {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.getUserById(userId);
        responseDTO.setData(userDto);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_USER_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    };
    @GetMapping("admin/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getUserByIdRoleAdmin(@PathVariable(name = "id") String credentialId)  {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.getUserByIdRoleAdmin(credentialId);
        responseDTO.setData(userDto);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_USER_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    };
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(name = "id") String userId) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.deleteUserById(userId);
        if (checkDTO != null) {
            responseDTO.setSuccessMessage(UserSuccessMessage.DELETE_USER_SUCCESS);
            responseDTO.setData(checkDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<ResponseDto> add(@Validated @RequestBody AddUserDto user)
    {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.addUser(user);
        responseDTO.setData(userDto);
            responseDTO.setSuccessMessage(UserSuccessMessage.ADD_USER_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping("/addAdmin")
    public ResponseEntity<ResponseDto> addAdmin(@Validated @RequestBody AddUserDto user)
    {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.addAdmin(user);
        responseDTO.setData(userDto);
            responseDTO.setSuccessMessage(UserSuccessMessage.ADD_ADMIN_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping("/addFactory")
    public ResponseEntity<ResponseDto> addFactory(@Validated @RequestBody AddUserDto user)
    {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.addFactory(user);
        responseDTO.setData(userDto);
            responseDTO.setSuccessMessage(FactorySuccessMessage.ADD_FACTORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PutMapping("update/{id}")
    public ResponseEntity<ResponseDto> update(@Validated @RequestBody UpdateUserDto user, @PathVariable(name = "id") String id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updateUser(user, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PutMapping("update-by-admin/{id}")
    public ResponseEntity<ResponseDto> updateByAdmin(@Validated @RequestBody UpdateUserDtoByAdmin user, @PathVariable(name = "id") String id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updateUserByAdmin(user, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PatchMapping("password-change/{id}")
    public ResponseEntity<ResponseDto> updatePassword(@Validated @RequestBody UpdatePasswordDto user, @PathVariable(name = "id") String id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updatePassword(user, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER)
    @PatchMapping("avatar/{id}")
    public ResponseEntity<ResponseDto> updateAvatar(@Validated @RequestBody UpdateAvatarDto avatar, @PathVariable(name = "id") String id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updateAvatar(avatar, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<ResponseDto> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        ResponseDto<Page<UserDto>> responseDTO = new ResponseDto();
        Page<UserDto> users = userService.getAllUser(pageNumber, pageSize);
        responseDTO.setData(users);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_ALL_USER_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping("email")
    public ResponseEntity<ResponseDto> findByEmail(@RequestParam String email) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto user = userService.findByEmail(email);
        responseDTO.setData(user);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_USER_BY_EMAIL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping("role")
    public ResponseEntity<ResponseDto> findByRoleName(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam String roleName) {
        ResponseDto<Page<UserDto>> responseDTO = new ResponseDto();
        Page<UserDto> userPage = userService.getAllByRoleName(pageNumber, pageSize, roleName);
        responseDTO.setData(userPage);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_ALL_USER_BY_ROLE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER_AND_FACTORY)
    @GetMapping("verify")
    public ResponseEntity<ResponseDto> sendEmailToVerify() {
        ResponseDto<Void> responseDTO = new ResponseDto();
        emailService.sendEmail();
        responseDTO.setSuccessMessage(UserSuccessMessage.SEND_LINK_VERIFY_TO_EMAIL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PermitAll
    @GetMapping("forgot-password")
    public ResponseEntity<ResponseDto> sendEmailToGetPassword(@RequestParam String email){
        ResponseDto<Void> responseDTO = new ResponseDto();
        emailService.sendEmailToGetBackPassword(email);
        responseDTO.setSuccessMessage(UserSuccessMessage.SEND_LINK_VERIFY_TO_GET_BACK_PASSWORD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PermitAll
    @PutMapping("reset")
    public ResponseEntity<ResponseDto> resetPassword(@Validated @RequestBody ResetPasswordDto resetPasswordDto){
        ResponseDto<Void> responseDTO = new ResponseDto();
        emailService.resetPassword(resetPasswordDto.getEmail(), resetPasswordDto.getToken(), resetPasswordDto.getNewPassword());
        responseDTO.setSuccessMessage(UserSuccessMessage.RESET_PASSWORD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER_AND_FACTORY)
    @PatchMapping("confirm/{email}/{token}")
    public ResponseEntity<ResponseDto> confirm(@PathVariable(name = "email") String email,@PathVariable(name = "token") String token) {
        ResponseDto<Page<UserDto>> responseDTO = new ResponseDto();
        emailService.confirm(email,token);
        responseDTO.setSuccessMessage(UserSuccessMessage.VERIFY_EMAIL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
