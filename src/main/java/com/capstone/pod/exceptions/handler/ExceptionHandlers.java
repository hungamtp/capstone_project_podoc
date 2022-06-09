package com.capstone.pod.exceptions.handler;

import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlers extends RuntimeException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(value = {UsernameOrPasswordNotFoundException.class, AuthenticationException.class})
    public ResponseEntity<Object> usernameOrPasswordNotFound(AuthenticationException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(UserErrorMessage.EMAIL_OR_PASSWORD_INCORRECT);
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = CategoryExistedException.class)
    public ResponseEntity<Object> categoryExistedException(CategoryExistedException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> usernameNotFoundException(UsernameNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {RoleNotFoundException.class})
    public ResponseEntity<Object> roleNotFoundException(RoleNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {UserNameExistException.class})
    public ResponseEntity<Object> userNameExistException(UserNameExistException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = EmailExistException.class)
    public ResponseEntity<Object> userNameExistException(EmailExistException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {CategoryNotFoundException.class})
    public ResponseEntity<Object> categoryNotFoundException(CategoryNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {ProductNameExistException.class})
    public ResponseEntity<Object> productNameExistException(ProductNameExistException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<Object> productNotFoundException(ProductNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> userNotFoundException(UserNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> notFoundException(NoSuchElementException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage("Object not found");
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(value = UserDisableException.class)
    public ResponseEntity<Object> notFoundException(UserDisableException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = EmailNotFoundException.class)
    public ResponseEntity<Object> emailNotFoundException(EmailNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = PermissionException.class)
    public ResponseEntity<Object> permissionException(PermissionException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = CredentialNotFoundException.class)
    public ResponseEntity<Object> credentialNotFound(CredentialNotFoundException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = CredentialDeletedAlreadyException.class)
    public ResponseEntity<Object> credentialDeleteAlready(CredentialDeletedAlreadyException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = PasswordNotMatchException.class)
    public ResponseEntity<Object> passwordNotMatch(PasswordNotMatchException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }
    @ExceptionHandler(value = DesignedProductNotExistException.class)
    public ResponseEntity<Object> passwordNotMatch(DesignedProductNotExistException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }


}
