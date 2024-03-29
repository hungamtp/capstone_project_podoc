package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.dto.sizeproduct.AddSizeProductDto;
import com.capstone.pod.dto.sizeproduct.EditSizeProductDto;
import com.capstone.pod.dto.sizeproduct.SizeProductDto;
import com.capstone.pod.entities.Product;
import com.capstone.pod.entities.SizeProduct;
import com.capstone.pod.exceptions.ProductNotFoundException;
import com.capstone.pod.repositories.ProductRepository;
import com.capstone.pod.repositories.SizeProductRepository;
import com.capstone.pod.services.SizeProductService;
import com.capstone.pod.utils.SizeUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SizeProductServiceImpl implements SizeProductService {

    private SizeProductRepository sizeProductRepository;
    private ProductRepository productRepository;

    @Override
    public AddSizeProductDto addSizeProduct(AddSizeProductDto addSizeProductDto) {
        Product product = productRepository.findById(addSizeProductDto.getProductId()).orElseThrow(
            () -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));

        Optional<SizeProduct> sizeProduct = sizeProductRepository.findByProductAndSize(product, addSizeProductDto.getSize());

        if (sizeProduct.isPresent()) {
            throw new IllegalStateException(EntityName.SIZE_PRODUCT + "_" + ErrorMessage.EXIST);
        }

        sizeProductRepository.save(
            SizeProduct.builder()
                .size(addSizeProductDto.getSize())
                .height(addSizeProductDto.getHeight())
                .width(addSizeProductDto.getWidth())
                .product(product)
                .build()
        );
        return addSizeProductDto;
    }

    @Override
    public EditSizeProductDto editSizeProduct(EditSizeProductDto editSizeProductDto) {
        SizeProduct sizeProduct = sizeProductRepository.findById(editSizeProductDto.getId()).orElseThrow(
            () -> new EntityNotFoundException(EntityName.SIZE_PRODUCT + "_" + ErrorMessage.NOT_FOUND));
        sizeProduct.setSize(editSizeProductDto.getSize());
        sizeProduct.setHeight(editSizeProductDto.getHeight());
        sizeProduct.setWidth(editSizeProductDto.getWidth());
        sizeProductRepository.save(sizeProduct);
        return editSizeProductDto;
    }

    @Override
    public boolean delete(String sizeProductId) {
        sizeProductRepository.findById(sizeProductId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.SIZE_PRODUCT + "_" + ErrorMessage.NOT_FOUND));
        sizeProductRepository.deleteById(sizeProductId);
        return true;
    }

    @Override
    public List<SizeProductDto> getAllSizeProductByProductId(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));

        List<SizeProduct> sizeProducts = sizeProductRepository.findALlByProduct(product);

        for (int i = 0; i < sizeProducts.size(); i++) {
            for (int j = i + 1; j < sizeProducts.size(); j++) {
                if (SizeUtils.sizes.get(sizeProducts.get(i).getSize()) != null && SizeUtils.sizes.get(sizeProducts.get(j).getSize()) != null)
                    if (SizeUtils.sizes.get(sizeProducts.get(i).getSize()) > SizeUtils.sizes.get(sizeProducts.get(j).getSize())) {
                        var sizeTemp = sizeProducts.get(i);
                        sizeProducts.set(i, sizeProducts.get(j));
                        sizeProducts.set(j, sizeTemp);
                    }
            }
        }

        return sizeProducts.stream().map(sizeProduct -> {
            return SizeProductDto.builder()
                .id(sizeProduct.getId())
                .size(sizeProduct.getSize())
                .height(sizeProduct.getHeight())
                .width(sizeProduct.getWidth())
                .build();
        }).collect(Collectors.toList());

    }


}
