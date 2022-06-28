package com.capstone.pod.services.implement;

import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.size.SizeDto;
import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.dto.sizecolor.SizeColorReturnDto;
import com.capstone.pod.entities.Color;
import com.capstone.pod.entities.Product;
import com.capstone.pod.entities.Size;
import com.capstone.pod.entities.SizeColor;
import com.capstone.pod.exceptions.ColorNotFoundException;
import com.capstone.pod.exceptions.ProductNotFoundException;
import com.capstone.pod.exceptions.SizeExistedException;
import com.capstone.pod.exceptions.SizeNotFoundException;
import com.capstone.pod.repositories.ColorRepository;
import com.capstone.pod.repositories.ProductRepository;
import com.capstone.pod.repositories.SizeColorRepository;
import com.capstone.pod.repositories.SizeRepository;
import com.capstone.pod.services.SizeColorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeColorServiceImplement implements SizeColorService {
    private final SizeColorRepository sizeColorRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<SizeColorReturnDto> addSizeColor(int productId, SizeColorDto dto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        List<Color> colors = new ArrayList<>();
        List<Size> sizes = new ArrayList<>();
        for (int i = 0; i < dto.getColors().size(); i++) {
            Color color = colorRepository.findByName(dto.getColors().get(i)).orElseThrow(() -> new ColorNotFoundException(ProductErrorMessage.COLOR_NOT_FOUND));
            colors.add(color);
        }
        for (int i = 0; i < dto.getSizes().size(); i++) {
            Size size = sizeRepository.findByName(dto.getSizes().get(i)).orElseThrow(() -> new SizeNotFoundException(ProductErrorMessage.SIZE_NOT_FOUND));
            sizes.add(size);
        }
        List<SizeColor> sizeColors = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            for (int j = 0; j < sizes.size(); j++) {
                SizeColor sizeColor = SizeColor.builder().id((int) Math.random()*1000+i+j).product(product).color(colors.get(i)).size(sizes.get(j)).build();
                   sizeColors.add(sizeColor);
            }
        }
        List<SizeColor> productSizeColorList = product.getSizeColors();
        for (int i = 0; i < product.getSizeColors().size(); i++) {
            for (int j = 0; j < sizeColors.size(); j++) {
                if(productSizeColorList.get(i).getSize().getName().equals(sizeColors.get(j).getSize().getName())
                        && productSizeColorList.get(i).getColor().getName().equals(sizeColors.get(j).getColor().getName())){
                    sizeColors.remove(j);
                }
            }
        }
        sizeColorRepository.saveAll(sizeColors);
       return sizeColors.stream().map(sizeColor ->  modelMapper.map(sizeColor,SizeColorReturnDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<SizeDto> getAllSize(Pageable pageable) {
        Page<Size> sizes = sizeRepository.findAll(pageable);
        Page<SizeDto> sizeDtos = sizes.map(size -> modelMapper.map(size,SizeDto.class));
        return sizeDtos;
    }

    @Override
    public SizeDto addSize(SizeDto sizeDto) {
        Optional<Size> sizeInRepo =  sizeRepository.findByName(sizeDto.getName());
        if(sizeInRepo.isPresent()) throw new SizeExistedException(String.format(SizeColorErrorMessage.SIZE_EXISTED_EXCEPTION,sizeDto.getName()));
        Size size = Size.builder().name(sizeDto.getName()).build();
        return modelMapper.map(sizeRepository.save(size),SizeDto.class);
    }

    @Override
    public Page<ColorDto> getAllColor(Pageable pageable) {
        Page<Color> colors = colorRepository.findAll(pageable);
        Page<ColorDto> colorDtos = colors.map(color -> modelMapper.map(color,ColorDto.class));
        return colorDtos;
    }

    @Override
    public ColorDto addColor(ColorDto colorDto) {
        Optional<Color> colorInRepo =  colorRepository.findByName(colorDto.getName());
        if(colorInRepo.isPresent()) throw new SizeExistedException(String.format(SizeColorErrorMessage.COLOR_EXISTED_EXCEPTION,colorDto.getName()));
        Color color = Color.builder().imageColor(colorDto.getImageColor()).name(colorDto.getName()).build();
        return modelMapper.map(colorRepository.save(color),ColorDto.class);
    }
}
