package com.capstone.pod.controller.cart;

import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.cartdetail.CartDetailDTO;
import com.capstone.pod.services.CartService;
import com.capstone.pod.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;

    @GetMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity getCart(HttpServletRequest request){
        String jwt =request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer " , ""));
        return ResponseEntity.ok().body(cartService.getCard(email));
    }

    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public void updateCart(HttpServletRequest request , @RequestBody List<CartDetailDTO> cartDetailDTO){
        String jwt =request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer " , ""));
         cartService.updateCart(cartDetailDTO,email);
    }

    @DeleteMapping("/{cartDetailId}")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public void deleteCartDetail(HttpServletRequest request , @PathVariable int cartDetailId){
        String jwt =request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer " , ""));
        cartService.deleteCartDetail(cartDetailId,email);
    }
}
