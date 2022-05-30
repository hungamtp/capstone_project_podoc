package com.capstone.pod.controller.user;

import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserSuccessMessage;
import com.capstone.pod.dto.credential.CredentialDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.user.*;
import com.capstone.pod.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<ResponseDto> getUserById(@PathVariable(name = "id") int userId)  {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.getUserById(userId);
        responseDTO.setData(userDto);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_USER_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    };
    @GetMapping("admin/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getUserByIdRoleAdmin(@PathVariable(name = "id") int userId)  {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.getUserByIdRoleAdmin(userId);
        responseDTO.setData(userDto);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_USER_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    };
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(name = "id") int userId) {
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
            responseDTO.setSuccessMessage(UserSuccessMessage.ADD_ADMIN_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping("update/{id}")
    public ResponseEntity<ResponseDto> update(@Validated @RequestBody UpdateUserDto user, @PathVariable(name = "id") int id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updateUser(user, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping("update-by-admin/{id}")
    public ResponseEntity<ResponseDto> updateByAdmin(@Validated @RequestBody UpdateUserDtoByAdmin user, @PathVariable(name = "id") int id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updateUserByAdmin(user, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping("password-change/{id}")
    public ResponseEntity<ResponseDto> updatePassword(@Validated @RequestBody UpdatePasswordDto user, @PathVariable(name = "id") int id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updatePassword(user, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER)
    @PostMapping("avatar/{id}")
    public ResponseEntity<ResponseDto> updateAvatar(@Validated @RequestBody UpdateAvatarDto avatar, @PathVariable(name = "id") int id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.updateAvatar(avatar, id);
        responseDTO.setSuccessMessage(UserSuccessMessage.UPDATE_USER_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping()
    public ResponseEntity<ResponseDto> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        ResponseDto<Page<UserDto>> responseDTO = new ResponseDto();
        Page<UserDto> users = userService.getAllUser(pageNumber, pageSize);
        responseDTO.setData(users);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_ALL_USER_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping("email/{email}")
    public ResponseEntity<ResponseDto> findByEmail(@PathVariable(name = "email") String email) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
//        UserDto user = userService.findByEmail(email);
        responseDTO.setData(null);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_USER_BY_EMAIL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
