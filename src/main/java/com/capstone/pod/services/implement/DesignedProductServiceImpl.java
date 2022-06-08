package com.capstone.pod.services.implement;

import com.capstone.pod.dto.designedProduct.DesignedProductDTO;
import com.capstone.pod.dto.designedProduct.DesignedProductDetailDTO;
import com.capstone.pod.dto.designedProduct.DesignedProductSaveDto;
import com.capstone.pod.entities.BluePrint;
import com.capstone.pod.entities.DesignInfo;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.Placeholder;
import com.capstone.pod.repositories.DesignedProductRepository;
import com.capstone.pod.services.DesignedProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignedProductServiceImpl implements DesignedProductService {
    private final DesignedProductRepository designedProductRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DesignedProductDTO> get4HighestRateDesignedProduct() {
        List<DesignedProductDetailDTO> designedProductDetailDTOS = designedProductRepository.get4HighestRateDesignedProduct();
        return calculate4HighestRateDesignedProduct(designedProductDetailDTOS);
//        Map<Integer, List<DesignedProductDetailDTO>> mapByDesignProducts = designedProductDetailDTOS.stream()
//            .collect(Collectors.groupingBy(d -> d.getId()));

//        for (Map.Entry<Integer, List<DesignedProductDetailDTO>> designProductDetail : mapByDesignProducts.entrySet()) {
//
//            DesignedProductDTO designedProductDTO = DesignedProductDTO.builder()
//                .id(designProductDetail.getKey())
//                .rate(designProductDetail.getValue().get(0).getRate())
//                .name(designProductDetail.getValue().get(0).getName())
//                .image(designProductDetail.getValue().get(0).getImage())
//                .designedPrice(designProductDetail.getValue().get(0).getDesignedPrice())
//                .tags(designProductDetail.getValue().stream().map(d -> d.getTag()).collect(Collectors.toList()))
//                .build();
//
//            result.add(designedProductDTO);
//        }
    }

    @Override
    public List<DesignedProductDTO> get4HighestRateDesignedProductByProductId(int productId) {
        List<DesignedProductDetailDTO> designedProductDetailDTOS = designedProductRepository.get4HighestRateDesignedProductByProductId(productId);
        return calculate4HighestRateDesignedProduct(designedProductDetailDTOS);

    }



    private List<DesignedProductDTO> calculate4HighestRateDesignedProduct(List<DesignedProductDetailDTO> designedProductDetailDTOS){
        List<DesignedProductDTO> result = new ArrayList<>();

        for (var designProductDetail : designedProductDetailDTOS) {
            if (result.size() == 5) return result.subList(0 , 4);
            if(result.size() == 0){
                DesignedProductDTO designedProductDTO = DesignedProductDTO.builder()
                    .id(designProductDetail.getId())
                    .rate(designProductDetail.getRate())
                    .name(designProductDetail.getName())
                    .image(designProductDetail.getImage())
                    .designedPrice(designProductDetail.getDesignedPrice())
                    .tags(new ArrayList<>())
                    .build();
                if(null != designProductDetail.getTag()){
                    designedProductDTO.getTags().add(designProductDetail.getTag());
                }
                result.add(designedProductDTO);
            }
            else {
                var currentDesign = result.get(result.size() - 1);
                if (designProductDetail.getId() == currentDesign.getId()) {
                    currentDesign.getTags().add(designProductDetail.getTag());
                } else {
                    DesignedProductDTO designedProductDTO = DesignedProductDTO.builder()
                        .id(designProductDetail.getId())
                        .rate(designProductDetail.getRate())
                        .name(designProductDetail.getName())
                        .image(designProductDetail.getImage())
                        .designedPrice(designProductDetail.getDesignedPrice())
                        .tags(new ArrayList<>())
                        .build();
                    if(null != designProductDetail.getTag()){
                        designedProductDTO.getTags().add(designProductDetail.getTag());
                    }
                    result.add(designedProductDTO);
                }
            }

        }
        return result;
    }
    @Override
    public DesignedProductSaveDto addDesignedProduct(DesignedProductSaveDto dto) {
        DesignedProduct designedProduct = DesignedProduct.builder().designedPrice(dto.getDesignedPrice()).name(dto.getName()).build();
        for (int i = 0; i < dto.getBluePrintDtos().size(); i++) {
            Placeholder placeholder = Placeholder.builder()
                    .height(dto.getBluePrintDtos().get(i).getPlaceholder().getHeight())
                    .width(dto.getBluePrintDtos().get(i).getPlaceholder().getWidth()).build();
            BluePrint bluePrint = BluePrint.builder()
                    .frameImage(dto.getBluePrintDtos().get(i).getFrameImage())
                    .position(dto.getBluePrintDtos().get(i).getPosition())
                    .placeholder(placeholder)
                    .designedProduct(designedProduct)
                    .build();
            for (int j = 0; j < dto.getBluePrintDtos().get(i).getDesignInfoDtos().size(); j++) {
                DesignInfo designInfo = DesignInfo.builder()
                        .bluePrint(bluePrint)
                        .name(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getName())
                        .types(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getTypes())
                        .height(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getHeight())
                        .width(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getWidth())
                        .leftPosition(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getLeftPosition())
                        .topPosition(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getTopPosition())
                        .x(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getX())
                        .y(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getY())
                        .rotate(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getRotate())
                        .scales(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getScales())
                        .src(dto.getBluePrintDtos().get(i).getDesignInfoDtos().get(j).getSrc())
                        .build();
                bluePrint.getDesignInfos().add(designInfo);
            }
            designedProduct.getBluePrints().add(bluePrint);
        }
        return modelMapper.map(designedProductRepository.save(designedProduct),DesignedProductSaveDto.class);
    }
}
