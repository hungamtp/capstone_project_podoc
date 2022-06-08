package com.capstone.pod.controller.designedproduct;

import com.capstone.pod.constant.designedproduct.DesignedProductSuccessMessage;
import com.capstone.pod.dto.designedProduct.DesignedProductDTO;
import com.capstone.pod.dto.designedProduct.DesignedProductSaveDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.services.DesignedProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("design")
public class DesignedProductController {
    private DesignedProductService designedProductService;

    @GetMapping("/4highestDRateDesignedProduct")
    public ResponseEntity<ResponseDto> get4highestDRateDesignedProduct() {
        ResponseDto<List<DesignedProductDTO>> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.get4HighestRateDesignedProduct());
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/4highestDRateDesignedProduct/{productId}")
    public ResponseEntity<ResponseDto> get4highestDRateDesignedProductByProductId(@PathVariable int productId) {
        ResponseDto<List<DesignedProductDTO>> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.get4HighestRateDesignedProductByProductId(productId));
        return ResponseEntity.ok().body(responseDto);
    }
    @PostMapping
    public ResponseEntity<ResponseDto> addDesignedProduct(@RequestBody DesignedProductSaveDto dto) {
        ResponseDto<DesignedProductSaveDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.addDesignedProduct(dto));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.ADD_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
