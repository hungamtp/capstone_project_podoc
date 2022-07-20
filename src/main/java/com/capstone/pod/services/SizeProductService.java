package com.capstone.pod.services;

import com.capstone.pod.dto.sizeproduct.AddSizeProductDto;
import com.capstone.pod.dto.sizeproduct.EditSizeProductDto;
import com.capstone.pod.dto.sizeproduct.SizeProductDto;

import java.util.List;

public interface SizeProductService {
    AddSizeProductDto addSizeProduct(AddSizeProductDto addSizeProductDto);
    EditSizeProductDto editSizeProduct(EditSizeProductDto editSizeProductDto);
    boolean delete(String sizeProductId);
    List<SizeProductDto> getAllSizeProductByProductId(String productId);
}
