package com.capstone.pod.services;

import com.capstone.pod.dto.material.MaterialDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {
    MaterialDto addMaterial(MaterialDto dto);
    MaterialDto editMaterial(MaterialDto dto);
    Page<MaterialDto> getAllMaterial(Pageable pageable);
    void deleteMaterial(String id);
}
