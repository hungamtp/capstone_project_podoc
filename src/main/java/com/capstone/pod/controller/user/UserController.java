package com.capstone.pod.controller.user;

import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserSuccessMessage;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.user.AddUserDto;
import com.capstone.pod.dto.user.UpdateUserDto;
import com.capstone.pod.dto.user.UpdateUserDtoByAdmin;
import com.capstone.pod.dto.user.UserDto;
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
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER)
    public ResponseEntity<ResponseDto> getUserById(@PathVariable(name = "id") int userId)  {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto userDto = userService.getUserById(userId);
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
        }
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> add(@Validated @RequestBody AddUserDto user)
    {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = userService.addUser(user);
        if (checkDTO != null) {
            responseDTO.setSuccessMessage(UserSuccessMessage.ADD_USER_SUCCESS);
        }
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping("/{id}")
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
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping()
    public ResponseEntity<ResponseDto> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        ResponseDto<Page<UserDto>> responseDTO = new ResponseDto();
        Page<UserDto> users = userService.getAllUser(pageNumber, pageSize);
        responseDTO.setData(users);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_ALL_USER_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
