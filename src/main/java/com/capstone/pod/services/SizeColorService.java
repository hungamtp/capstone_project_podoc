package com.capstone.pod.services;

import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.size.SizeDto;
import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.dto.sizecolor.SizeColorReturnDto;

import java.util.List;

public interface SizeColorService {
    public List<SizeColorReturnDto> addSizeColor(int productId, SizeColorDto dto);
    public List<SizeDto> getSizes();
    public List<ColorDto> getColors();
}
