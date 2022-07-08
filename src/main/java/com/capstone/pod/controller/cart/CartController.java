package com.capstone.pod.controller.cart;

import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.cartdetail.AddToCartDto;
import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.dto.common.ResponseDto;
import com.capstone.pod.services.CartService;
import com.capstone.pod.dto.utils.Utils;
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

    @PutMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public void updateCart(HttpServletRequest request, @RequestBody List<CartDetailDto> cartDetailDTO) {
        String jwt = request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer ", ""));
        cartService.updateCart(cartDetailDTO, email);
    }

    @DeleteMapping("/{cartDetailId}")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity deleteCartDetail(HttpServletRequest request , @PathVariable String cartDetailId){
        String jwt =request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer " , ""));
        return ResponseEntity.ok().body(cartService.deleteCartDetail(cartDetailId,email));
    }

    @PutMapping("/checkQuantity")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity checkQuantityCart(HttpServletRequest request, @RequestBody List<CartDetailDto> cartDetailDTO) {
        String jwt = request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer ", ""));
        return ResponseEntity.ok().body(cartService.checkQuantityBeforeOrder(cartDetailDTO, email));
    }

    @PutMapping("/addToCart")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity addToCart(HttpServletRequest request, @RequestBody AddToCartDto addToCartDto) {
        String jwt = request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer ", ""));
        ResponseDto responseDTO = new ResponseDto();
        responseDTO.setData(cartService.addToCart(addToCartDto , email));
        return ResponseEntity.ok().body(responseDTO);
    }
}
