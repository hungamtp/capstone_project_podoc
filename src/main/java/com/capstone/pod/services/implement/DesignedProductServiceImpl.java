package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.designedproduct.DesignedProductErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignedProductServiceImpl implements DesignedProductService {
    private final DesignedProductRepository designedProductRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final ColorRepository colorRepository;
    private final DesignColorRepository designColorRepository;
    private final PriceByFactoryRepository priceByFactoryRepository;
    private final RatingRepository ratingRepository;
    private final DesignedProductTagRepository designedProductTagRepository;
    private final OrderDetailRepository orderDetailRepository;

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Optional<Credential> credential = credentialRepository.findById(currentCredentialId);
        return credential.get().getUser();
    }
    @Override
    public DesignedProductReturnDto addDesignedProduct(DesignedProductSaveDto dto, int productId,int factoryId) {
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
            imagePreviews.add(ImagePreview.builder().image(dto.getImagePreviews().get(i).getImage()).position(dto.getImagePreviews().get(i).getPosition()).build());
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
    public DesignedProductReturnDto editDesignedProduct(DesignedProductSaveDto dto, int designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProductInRepo = designedProductRepository.findById(designId).orElseThrow(()->new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProductInRepo.setDesignedPrice(dto.getDesignedPrice());
        List<ImagePreview> imagePreviews = new ArrayList<>();
        for (int i = 0; i < dto.getImagePreviews().size(); i++) {
            imagePreviews.add(ImagePreview.builder().image(dto.getImagePreviews().get(i).getImage()).position(dto.getImagePreviews().get(i).getPosition()).build());
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
    public DesignedProductReturnDto publishDesignedProduct(int designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProduct.setPublish(true);
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
    public DesignedProductReturnDto getDesignedProductById(int designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        DesignedProductReturnDto designedProductReturnDto = modelMapper.map(designedProduct,DesignedProductReturnDto.class);
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < designedProduct.getDesignColors().size(); i++) {
            colors.add(designedProduct.getDesignColors().get(i).getColor().getName());
        }
        designedProductReturnDto.setColors(colors);
        designedProductReturnDto.setPriceFromFactory(designedProduct.getPriceByFactory().getPrice());
        return designedProductReturnDto;
    }

    @Override
    public DesignedProductReturnDto unPublishDesignedProduct(int designId) {
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
    public DesignedProductReturnDto editDesignedProductPrice(DesignedProductPriceDto dto, int designId) {
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
    public Page<ViewOtherDesignDto> viewMyDesign(Pageable page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Page<DesignedProduct> designedProductPage = designedProductRepository.findAllByUserId(page, credential.getUser().getId());
        Page<ViewOtherDesignDto> viewOtherDesignDtoPage = designedProductPage.map(designedProduct -> modelMapper.map(designedProduct,ViewOtherDesignDto.class));
        return viewOtherDesignDtoPage;
    }
    @Override
    public Page<ViewAllDesignDto> viewAllDesign(Pageable page) {
        Page<DesignedProduct> designedProductPage = designedProductRepository.findAll(page);
        List<ViewAllDesignDto> viewAllDesignDtos = designedProductPage.stream().filter(designedProduct -> designedProduct.isPublish()==true).map(designedProduct -> ViewAllDesignDto.builder()
                .price(designedProduct.getDesignedPrice()+designedProduct.getPriceByFactory().getPrice())
                .user(modelMapper.map(designedProduct.getUser(), UserInDesignDto.class))
                .name(designedProduct.getName())
                .rating(ratingRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(rating -> rating.getRatingStar()).collect(Collectors.averagingDouble(num -> Double.parseDouble(num+""))))
                .tagName(designedProductTagRepository.findAllByDesignedProductId(designedProduct.getId()).stream().map(designedProductTag -> designedProductTag.getTag().getName()).collect(Collectors.toList()))
                .publish(designedProduct.isPublish())
                .imagePreviews(designedProduct.getImagePreviews().stream().map(imagePreview -> modelMapper.map(imagePreview, ImagePreviewDto.class)).collect(Collectors.toList()))
                .sold(orderDetailRepository.findAllByDesignedProductId(designedProduct.getId()).size())
                .build()).collect(Collectors.toList());
        Page<ViewAllDesignDto> dtoPage = new PageImpl<>(viewAllDesignDtos,page,viewAllDesignDtos.size());
        return dtoPage;
    }
    @Override
    public Page<ViewOtherDesignDto> viewOtherDesign(Pageable page, int userId) {

        Page<DesignedProduct> designedProductPage = designedProductRepository.findAllByUserId(page, userId);
        List<ViewOtherDesignDto> viewOtherDesignDtos = designedProductPage.stream().filter(designedProduct -> designedProduct.isPublish()==true).map(designedProduct -> ViewOtherDesignDto.builder()
                .price(designedProduct.getDesignedPrice()+designedProduct.getPriceByFactory().getPrice())
                .user(modelMapper.map(designedProduct.getUser(), UserInDesignDto.class))
                .name(designedProduct.getName())
                .publish(designedProduct.isPublish())
                .imagePreviews(designedProduct.getImagePreviews().stream().map(imagePreview -> modelMapper.map(imagePreview, ImagePreviewDto.class)).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
        Page<ViewOtherDesignDto> dtoPage = new PageImpl<>(viewOtherDesignDtos,page,viewOtherDesignDtos.size());
        return dtoPage;
    }

    @Override
    public void deleteDesignedProduct(int designId) {
        if(!isPermittedUser(designId)) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        DesignedProduct designedProduct = designedProductRepository.findById(designId)
                .orElseThrow(() -> new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        designedProductRepository.delete(designedProduct);

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
    public List<DesignedProductDto> get4HighestRateDesignedProductByProductId(int productId) {
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

    private  boolean isPermittedUser(int designId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        DesignedProduct designedProduct = designedProductRepository.findById(designId).orElseThrow(()->new DesignedProductNotExistException(DesignedProductErrorMessage.DESIGNED_PRODUCT_NOT_EXIST));
        if(designedProduct.getUser().getId() == credential.getUser().getId()) return true;
        return false;
    }

}
