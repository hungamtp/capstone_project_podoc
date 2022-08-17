package com.capstone.pod.services.implement;

import com.capstone.pod.constant.material.MaterialErrorMessage;
import com.capstone.pod.dto.material.MaterialDto;
import com.capstone.pod.entities.Material;
import com.capstone.pod.exceptions.MaterialException;
import com.capstone.pod.repositories.MaterialRepository;
import com.capstone.pod.services.MaterialService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImplement implements MaterialService {
    private final MaterialRepository materialRepository;
    private final ModelMapper modelMapper;
    @Override
    public MaterialDto addMaterial(MaterialDto dto) {
        Optional<Material> materialOptional = materialRepository.findByName(dto.getName());
        if(materialOptional.isPresent()) throw new MaterialException(MaterialErrorMessage.MATERIAL_EXISTED_EXCEPTION);
      Material material = Material.builder().name(dto.getName()).build();
      return modelMapper.map(materialRepository.save(material),MaterialDto.class);
    }

    @Override
    public MaterialDto editMaterial(MaterialDto dto) {
        Material material= materialRepository.findById(dto.getId()).orElseThrow(() -> new MaterialException(MaterialErrorMessage.MATERIAL_NOT_EXISTED_EXCEPTION));
        List<Material> materials = materialRepository.findAll();
        for (int i = 0; i < materials.size(); i++) {
            if(materials.get(i).getName().equals(dto.getName())) throw new MaterialException(MaterialErrorMessage.MATERIAL_NAME_EXISTED_EXCEPTION);
        }
        material.setName(dto.getName());
        return modelMapper.map(materialRepository.save(material),MaterialDto.class);
    }

    @Override
    public Page<MaterialDto> getAllMaterial(Pageable pageable) {
        Page<Material> page = materialRepository.findAll(pageable);
        List<MaterialDto> list = page.stream().map(material -> modelMapper.map(material,MaterialDto.class)).collect(Collectors.toList());
        PageImpl pageReturn = new PageImpl(list,pageable,page.getTotalElements());
        return pageReturn;
    }

    @Override
    public void deleteMaterial(String id) {
        materialRepository.findById(id).orElseThrow(() -> new MaterialException(MaterialErrorMessage.MATERIAL_NOT_EXISTED_EXCEPTION));
        materialRepository.deleteById(id);
    }
}
