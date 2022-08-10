package com.capstone.pod.controller.material;

import com.capstone.pod.constant.material.MaterialSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.material.MaterialDto;
import com.capstone.pod.entities.Material_;
import com.capstone.pod.services.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("material")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<ResponseDto> getAll(@RequestParam int pageNumber, @RequestParam int pageSize)
    {
        ResponseDto<Page<MaterialDto>> responseDTO = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by(Material_.CREATE_DATE).descending());
        Page<MaterialDto> getAllMaterial = materialService.getAllMaterial(pageable);
        responseDTO.setData(getAllMaterial);
        responseDTO.setSuccessMessage(MaterialSuccessMessage.GET_ALL_MATERIAL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<ResponseDto> add(@RequestBody @Validated MaterialDto dto)
    {
        ResponseDto<MaterialDto> responseDTO = new ResponseDto();
        MaterialDto material = materialService.addMaterial(dto);
        responseDTO.setData(material);
        responseDTO.setSuccessMessage(MaterialSuccessMessage.ADD_MATERIAL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<ResponseDto> edit(@RequestBody @Validated MaterialDto dto)
    {
        ResponseDto<MaterialDto> responseDTO = new ResponseDto();
        MaterialDto material = materialService.editMaterial(dto);
        responseDTO.setData(material);
        responseDTO.setSuccessMessage(MaterialSuccessMessage.EDIT_MATERIAL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @DeleteMapping
    public ResponseEntity<ResponseDto> delete(@RequestParam String id)
    {
        ResponseDto<Void> responseDTO = new ResponseDto();
         materialService.deleteMaterial(id);
        responseDTO.setSuccessMessage(MaterialSuccessMessage.DELETE_MATERIAL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
