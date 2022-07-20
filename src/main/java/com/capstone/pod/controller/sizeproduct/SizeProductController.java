package com.capstone.pod.controller.sizeproduct;


import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.sizeproduct.AddSizeProductDto;
import com.capstone.pod.dto.sizeproduct.EditSizeProductDto;
import com.capstone.pod.services.SizeProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("size-product")
@PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
public class SizeProductController {
    private SizeProductService sizeProductService;

    @GetMapping("/{productId}")
    public ResponseEntity get(@PathVariable String productId) {
        return ResponseEntity.ok().body(sizeProductService.getAllSizeProductByProductId(productId));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody EditSizeProductDto editSizeProductDto){
        return ResponseEntity.ok().body(sizeProductService.editSizeProduct(editSizeProductDto));
    }

    @PostMapping
    public ResponseEntity add (@RequestBody AddSizeProductDto addSizeProductDto){
        return ResponseEntity.ok().body(sizeProductService.addSizeProduct(addSizeProductDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id){
        return ResponseEntity.ok().body(sizeProductService.delete(id));
    }

}
