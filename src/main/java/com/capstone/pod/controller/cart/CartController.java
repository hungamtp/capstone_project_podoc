package com.capstone.pod.controller.cart;

import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.services.CartService;
import com.capstone.pod.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;

    @GetMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity getEmailFromJwt(HttpServletRequest request){
        String jwt =request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer " , ""));
        return ResponseEntity.ok().body(cartService.getCard(email));
    }
}
