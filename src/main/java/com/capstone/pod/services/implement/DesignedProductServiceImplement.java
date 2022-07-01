package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.designedproduct.DesignedProductErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.color.ColorInDesignDto;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.designedProduct.*;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.user.UserInDesignDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.DesignedProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.View;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignedProductServiceImplement implements DesignedProductService {
    private final DesignedProductRepository designedProductRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CredentialRepository credentialRepository;
    private final ColorRepository colorRepository;
    private final DesignColorRepository designColorRepository;
    private final PriceByFactoryRepository priceByFactoryRepository;
    private final RatingRepository ratingRepository;
    private final DesignedProductTagRepository designedProductTagRepository;
    private final OrderDetailRepository orderDetailRepository;

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Optional<Credential> credential = credentialRepository.findById(currentCredentialId.toString());
        return credential.get().getUser();
    }
    @Override
    public DesignedProductReturnDto addDesignedProduct(DesignedProductSaveDto dto, String productId,String factoryId) {
        Product productInRepo = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        DesignedProduct designedProduct = DesignedProduct.builder().publish(false).product(productInRepo).designedPrice(dto.getDesignedPrice()).name(productInRepo.getName()).build();
        designedProduct.setDescription(dto.getDescription());
        designedProduct.setUser(getCurrentUser());
        designedProduct.setName(dto.getName());

        Optional<PriceByFactory> priceByFactory = priceByFactoryRepository.getByProductIdAndFactoryId(productId,factoryId);
        designedProduct.setPriceByFactory(priceByFactory.get());

        List<DesignColor> designColors = new ArrayList<>();
        for (int i = 0; i < dto.getColors().size(); i++) {
            Color color = colorRepository.findByImageColor(dto.getColors().get(i)).orElseThrow(() -> new ColorNotFoundException(ProductErrorMessage.COLOR_NOT_FOUND));
            DesignColor designColor = DesignColor.builder().color(color).designedProduct(designedProduct).build();
            designColors.add(designColor);
        }
        designedProduct.setDesignColors(designColors);

        List<ImagePreview> imagePreviews = new ArrayList<>();
        for (int i = 0; i < dto.getImagePreviews().size(); i++) {
            imagePreviews.add(ImagePreview.builder().designedProduct(designedProduct).image(dto.getImagePreviews().get(i).getImage()).color(dto.getImagePreviews().get(i).getColor()).position(dto.getImagePreviews().get(i).getPosition()).build());
        }
        designedProduct.setImagePreviews(imagePreviews);
        designedProduct.setBluePrints(new ArrayList<>());
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
            placeholder.setBluePrint(bluePrint);
            bluePrint.setDesignInfos(new ArrayList<>());
            for (int j = 0; j < dto.getBluePrintDtos().get(i).getDesignInfos().size(); j++) {
                DesignInfo designInfo = DesignInfo.builder()
                        .bluePrint(bluePrint)
                        .name(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getName())
                        .types(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getTypes())
                        .height(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getHeight())
                        .width(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getWidth())
                        .leftPosition(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getLeftPosition())
                        .topPosition(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getTopPosition())
                        .rotate(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getRotate())
                        .scales(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getScales())
                        .font(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getFont())
                        .textColor(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getTextColor())
                        .src(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getSrc())
                        .build();
                bluePrint.getDesignInfos().add(designInfo);
            }
            designedProduct.getBluePrints().add(bluePrint);
        }
        DesignedProductReturnDto designedProductReturnDto = modelMapper.map(designedProductRepository.save(designedProduct),DesignedProductReturnDto.class);
        designedProductReturnDto.setColors(dto.getColors());
        designedProductReturnDto.setPriceFromFactory(designedProduct.getPriceByFactory().getPrice());
        return designedProductReturnDto;
    }

    @Override
    public DesignedProductReturnDto editDesignedProduct(DesignedProductSaveDto dto, String designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProductInRepo = designedProductRepository.findById(designId).orElseThrow(()->new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProductInRepo.setDesignedPrice(dto.getDesignedPrice());
        List<ImagePreview> imagePreviews = new ArrayList<>();
        for (int i = 0; i < dto.getImagePreviews().size(); i++) {
            imagePreviews.add(ImagePreview.builder().designedProduct(designedProductInRepo).color(dto.getImagePreviews().get(i).getColor()).image(dto.getImagePreviews().get(i).getImage()).position(dto.getImagePreviews().get(i).getPosition()).build());
        }
        designedProductInRepo.setDescription(dto.getDescription());
        designedProductInRepo.setName(dto.getName());
        designColorRepository.deleteAllInBatch(designedProductInRepo.getDesignColors());
        List<DesignColor> designColors = new ArrayList<>();
        for (int i = 0; i < dto.getColors().size(); i++) {
            Color color = colorRepository.findByImageColor(dto.getColors().get(i)).orElseThrow(() -> new ColorNotFoundException(ProductErrorMessage.COLOR_NOT_FOUND));
            DesignColor designColor = DesignColor.builder().color(color).designedProduct(designedProductInRepo).build();
            designColors.add(designColor);
        }
        designedProductInRepo.setDesignColors(designColors);
        designedProductInRepo.setImagePreviews(imagePreviews);
        designedProductInRepo.setBluePrints(new ArrayList<>());
        for (int i = 0; i < dto.getBluePrintDtos().size(); i++) {
            Placeholder placeholder = Placeholder.builder()
                    .height(dto.getBluePrintDtos().get(i).getPlaceholder().getHeight())
                    .width(dto.getBluePrintDtos().get(i).getPlaceholder().getWidth()).build();
            BluePrint bluePrint = BluePrint.builder()
                    .frameImage(dto.getBluePrintDtos().get(i).getFrameImage())
                    .position(dto.getBluePrintDtos().get(i).getPosition())
                    .placeholder(placeholder)
                    .designedProduct(designedProductInRepo)
                    .build();
            placeholder.setBluePrint(bluePrint);
            bluePrint.setDesignInfos(new ArrayList<>());
            for (int j = 0; j < dto.getBluePrintDtos().get(i).getDesignInfos().size(); j++) {
                DesignInfo designInfo = DesignInfo.builder()
                        .bluePrint(bluePrint)
                        .name(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getName())
                        .types(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getTypes())
                        .height(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getHeight())
                        .width(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getWidth())
                        .leftPosition(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getLeftPosition())
                        .topPosition(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getTopPosition())
                        .rotate(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getRotate())
                        .scales(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getScales())
                        .src(dto.getBluePrintDtos().get(i).getDesignInfos().get(j).getSrc())
                        .build();
                bluePrint.getDesignInfos().add(designInfo);
            }
            designedProductInRepo.getBluePrints().add(bluePrint);
        }
        DesignedProductReturnDto designedProductReturnDto = modelMapper.map(designedProductRepository.save(designedProductInRepo),DesignedProductReturnDto.class);
        designedProductReturnDto.setColors(dto.getColors());
        designedProductReturnDto.setPriceFromFactory(designedProductInRepo.getPriceByFactory().getPrice());
        return designedProductReturnDto;
    }



    @Override
    public DesignedProductReturnDto getDesignedProductById(String designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        DesignedProductReturnDto designedProductReturnDto = modelMapper.map(designedProduct,DesignedProductReturnDto.class);
        Set<ColorInDesignDto> colors = designedProduct.getDesignColors().stream()
                .map(designColor -> ColorInDesignDto.builder()
                        .id(designColor.getId())
                        .image(designColor.getColor().getImageColor())
                        .name(designColor.getColor().getName()).build())
                .collect(Collectors.toSet());
        designedProductReturnDto.setColorsObj(colors);
        designedProductReturnDto.setPriceFromFactory(designedProduct.getPriceByFactory().getPrice());
        return designedProductReturnDto;
    }

    @Override
    public DesignedProductReturnDto unPublishDesignedProduct(String designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProduct.setPublish(false);
        DesignedProductReturnDto designedProductReturnDto = modelMapper.map(designedProductRepository.save(designedProduct),DesignedProductReturnDto.class);
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < designedProduct.getDesignColors().size(); i++) {
            colors.add(designedProduct.getDesignColors().get(i).getColor().getName());
        }
        designedProductReturnDto.setColors(colors);
        designedProductReturnDto.setPriceFromFactory(designedProduct.getPriceByFactory().getPrice());
        return designedProductReturnDto;
    }
    @Override
    public DesignedProductReturnDto editDesignedProductPrice(DesignedProductPriceDto dto, String designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProduct.setDesignedPrice(dto.getPrice());
        DesignedProductReturnDto designedProductReturnDto = modelMapper.map(designedProductRepository.save(designedProduct),DesignedProductReturnDto.class);
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < designedProduct.getDesignColors().size(); i++) {
            colors.add(designedProduct.getDesignColors().get(i).getColor().getName());
        }
        designedProductReturnDto.setColors(colors);
        designedProductReturnDto.setPriceFromFactory(designedProduct.getPriceByFactory().getPrice());
        return designedProductReturnDto;
    }

    @Override
    public Page<ViewMyDesignDto> viewMyDesign(Pageable page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId.toString()).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Page<DesignedProduct> designedProductPage = designedProductRepository.findAllByUserId(page, credential.getUser().getId());
        List<ViewMyDesignDto> viewMyDesignDtos = designedProductPage.stream().map(designedProduct -> ViewMyDesignDto.builder()
                .id(designedProduct.getId())
                .name(designedProduct.getName())
                .designedPrice(designedProduct.getDesignedPrice())
                .publish(designedProduct.isPublish())
                .imagePreviews(designedProduct.getImagePreviews().stream().map(imagePreview -> modelMapper.map(imagePreview, ImagePreviewDto.class)).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
        Page<ViewMyDesignDto> dtoPage = new PageImpl<>(viewMyDesignDtos,page,designedProductPage.getTotalElements());
        return dtoPage;
    }
    @Override
    public PageDTO viewAllDesign(Specification<DesignedProduct> specification, Pageable page) {
        Page<DesignedProduct> designedProductPage = designedProductRepository.findAll(specification ,page);
        List<ViewAllDesignDto> viewAllDesignDtos = designedProductPage.stream().map(designedProduct -> ViewAllDesignDto.builder()
                .id(designedProduct.getId())
                .price(designedProduct.getDesignedPrice()+designedProduct.getPriceByFactory().getPrice())
                .user(modelMapper.map(designedProduct.getUser(), UserInDesignDto.class))
                .name(designedProduct.getName())
                .rating(ratingRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(rating -> rating.getRatingStar()).collect(Collectors.averagingDouble(num -> Double.parseDouble(num+""))))
                .tagName(designedProductTagRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(designedProductTag -> designedProductTag.getTag().getName()).collect(Collectors.toList()))
                .publish(designedProduct.isPublish())
                .imagePreviews(designedProduct.getImagePreviews().stream().map(imagePreview -> modelMapper.map(imagePreview, ImagePreviewDto.class)).collect(Collectors.toList()))
                .sold(orderDetailRepository.findAllByDesignedProductId(designedProduct.getId()).size())
                .build()).collect(Collectors.toList());
        PageDTO dtoPage = PageDTO.builder().page(page.getPageNumber()).data(viewAllDesignDtos).elements((int) designedProductPage.getTotalElements()).build();
        return dtoPage;
    }
    @Override
    public Page<ViewOtherDesignDto> viewOtherDesign(Pageable page, String userId) {
        Page<DesignedProduct> designedProductPage = designedProductRepository.findAllByUserId(page, userId);
        List<ViewOtherDesignDto> viewOtherDesignDtos = designedProductPage.stream().filter(designedProduct -> designedProduct.isPublish()==true).map(designedProduct -> ViewOtherDesignDto.builder()
                .id(designedProduct.getId())
                .price(designedProduct.getDesignedPrice()+designedProduct.getPriceByFactory().getPrice())
                .user(modelMapper.map(designedProduct.getUser(), UserInDesignDto.class))
                .name(designedProduct.getName())
                .rating(ratingRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(rating -> rating.getRatingStar()).collect(Collectors.averagingDouble(num -> Double.parseDouble(num+""))))
                .publish(designedProduct.isPublish())
                .tagName(designedProductTagRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(designedProductTag -> designedProductTag.getTag().getName()).collect(Collectors.toList()))
                .sold(orderDetailRepository.findAllByDesignedProductId(designedProduct.getId()).size())
                .imagePreviews(designedProduct.getImagePreviews().stream().map(imagePreview -> modelMapper.map(imagePreview, ImagePreviewDto.class)).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
        Page<ViewOtherDesignDto> dtoPage = new PageImpl<>(viewOtherDesignDtos,page,viewOtherDesignDtos.size());
        return dtoPage;
    }

    @Override
    public ViewOtherDesignDto viewDesignDetailsByDesignId(String designId) {
        DesignedProduct designedProduct = designedProductRepository.findById(designId).orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        Set sizes = new HashSet<>();
        for (int i = 0; i < designedProduct.getProduct().getSizeColors().size(); i++) {
            List sizeByFactory = designedProduct.getProduct().getSizeColors().get(i).getSizeColorByFactories().stream().map(sizeColorByFactory -> sizeColorByFactory.getSizeColor().getSize().getName()).collect(Collectors.toList());
            sizes.add(sizeByFactory.get(0));
        }
        double price = 0;
        if(isPermittedUser(designId)){
            price = designedProduct.getPriceByFactory().getPrice();
        }
        else price = designedProduct.getDesignedPrice()+designedProduct.getPriceByFactory().getPrice();

        Set<ColorInDesignDto> colors = designedProduct.getDesignColors().stream()
                .map(designColor -> ColorInDesignDto.builder()
                        .id(designColor.getId())
                        .image(designColor.getColor().getImageColor())
                        .name(designColor.getColor().getName()).build())
                .collect(Collectors.toSet());
        ViewOtherDesignDto dto = ViewOtherDesignDto.builder()
                .id(designedProduct.getId())
                .factoryName(designedProduct.getPriceByFactory().getFactory().getName())
                .colors(colors)
                .sizes(sizes)
                .description(designedProduct.getDescription())
                .price(price)
                .user(modelMapper.map(designedProduct.getUser(), UserInDesignDto.class))
                .name(designedProduct.getName())
                .rating(ratingRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(rating -> rating.getRatingStar()).collect(Collectors.averagingDouble(num -> Double.parseDouble(num+""))))
                .publish(designedProduct.isPublish())
                .tagName(designedProductTagRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(designedProductTag -> designedProductTag.getTag().getName()).collect(Collectors.toList()))
                .sold(orderDetailRepository.findAllByDesignedProductId(designedProduct.getId()).size())
                .imagePreviews(designedProduct.getImagePreviews().stream().map(imagePreview -> modelMapper.map(imagePreview, ImagePreviewDto.class)).collect(Collectors.toList()))
                .build();
        return dto;
    }

    @Override
    public void deleteDesignedProduct(String designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProductRepository.delete(designedProduct);

    }

    @Override
    public boolean publishOrUnpublishDesign(String designId, String email , boolean publish) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(
            ()  -> new EntityNotFoundException(EntityName.CREDENTIAL + ErrorMessage.NOT_FOUND)
        );

        if(credential.getUser() == null)
            throw new EntityNotFoundException(EntityName.USER + ErrorMessage.NOT_FOUND);


        DesignedProduct designedProduct = designedProductRepository.findById(designId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.DESIGNED_PRODUCT + ErrorMessage.NOT_FOUND)
        );

        if(designedProduct.getUser().getId() != credential.getUser().getId()){
            throw new EntityNotFoundException(ValidationMessage.WRONG_OWNER);
        }

        designedProduct.setPublish(publish);
        designedProductRepository.save(designedProduct);
        return true;
    }

    @Override
    public List<DesignedProductDto> get4HighestRateDesignedProduct() {
        List<DesignedProductDetailDto> designedProductDetailDtos = designedProductRepository.get4HighestRateDesignedProduct();
        return calculate4HighestRateDesignedProduct(designedProductDetailDtos);
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
    public List<DesignedProductDto> get4HighestRateDesignedProductByProductId(String productId) {
        List<DesignedProductDetailDto> designedProductDetailDtos = designedProductRepository.get4HighestRateDesignedProductByProductId(productId);
        return calculate4HighestRateDesignedProduct(designedProductDetailDtos);
    }



    private List<DesignedProductDto> calculate4HighestRateDesignedProduct(List<DesignedProductDetailDto> designedProductDetailDtos){
        List<DesignedProductDto> result = new ArrayList<>();

        for (var designProductDetail : designedProductDetailDtos) {
            if (result.size() == 5) return result.subList(0 , 4);
            if(result.size() == 0){
                DesignedProductDto designedProductDTO = DesignedProductDto.builder()
                    .id(designProductDetail.getId())
                    .rate(designProductDetail.getRate()== null ? 0 :designProductDetail.getRate())
                    .name(designProductDetail.getName())
                    .image(designProductDetail.getImage())
                    .designedPrice(designProductDetail.getDesignedPrice())
                    .tags(new ArrayList<>())
                    .userId(designProductDetail.getUserId())
                    .username(designProductDetail.getUsername())
                    .soldCount(designProductDetail.getSoldCount() == null ? 0 :designProductDetail.getSoldCount() )
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
                    DesignedProductDto designedProductDTO = DesignedProductDto.builder()
                        .id(designProductDetail.getId())
                        .rate(designProductDetail.getRate()== null ? 0 :designProductDetail.getRate())
                        .name(designProductDetail.getName())
                        .image(designProductDetail.getImage())
                        .designedPrice(designProductDetail.getDesignedPrice())
                        .tags(new ArrayList<>())
                        .userId(designProductDetail.getUserId())
                        .username(designProductDetail.getUsername())
                        .soldCount(designProductDetail.getSoldCount() == null ? 0 :designProductDetail.getSoldCount() )
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

    private  boolean isPermittedUser(String designId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId.toString())
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        DesignedProduct designedProduct = designedProductRepository.findById(designId).orElseThrow(()->new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        if(designedProduct.getUser().getId() == credential.getUser().getId()) return true;
        return false;
    }

}
