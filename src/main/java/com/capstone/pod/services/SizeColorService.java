package com.capstone.pod.services;

import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.size.SizeDto;
import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.dto.sizecolor.SizeColorReturnDto;
import com.capstone.pod.entities.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SizeColorService {
    public List<SizeColorReturnDto> addSizeColor(int productId, SizeColorDto dto);
    public List<SizeDto> getSizes();
    public List<ColorDto> getColors();

    public Page<SizeDto> getAllSize(Pageable pageable);
    public SizeDto addSize(SizeDto sizeDto);
    public Page<ColorDto> getAllColor(Pageable pageable);
    public ColorDto addColor(ColorDto colorDto);

}
