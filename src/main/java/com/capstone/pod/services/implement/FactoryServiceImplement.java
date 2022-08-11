package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.factory.FactoryErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.factory.FactoryByIdDto;
import com.capstone.pod.dto.factory.FactoryPageResponseDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.order.OrderDetailFactoryDto;
import com.capstone.pod.dto.order.OrderDetailForPrintingDto;
import com.capstone.pod.dto.order.OrderDetailsSupportDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.dto.product.ProductImagesDto;
import com.capstone.pod.dto.sizecolor.SizeColorInFactoryDetailDto;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.FactoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FactoryServiceImplement implements FactoryService {
    private final FactoryRepository factoryRepository;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final PriceByFactoryRepository priceByFactoryRepository;
    private final ProductRepository productRepository;
    private final SizeColorRepository sizeColorRepository;
    private final SizeColorByFactoryRepository sizeColorByFactoryRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusRepository orderStatusRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ColorRepository colorRepository;
    private  Credential getPermittedCredential(String credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String)authentication.getCredentials();
        if(!currentCredentialId.equals(credential.getId())){
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        return credential;
    }

    @Override
    public Page<FactoryPageResponseDto> getAllFactories(Pageable pageable) {
        List<Credential> factoriesCredential = credentialRepository.findAll(pageable).stream().filter(credential -> credential.getFactory() != null).collect(Collectors.toList());
        List<FactoryPageResponseDto> credentialFactories= factoriesCredential.stream().filter(credential -> credential.getRole().getName().equals(RoleName.ROLE_FACTORY))
                .map(credential -> FactoryPageResponseDto.builder().email(credential.getEmail())
                        .id(credential.getFactory().getId())
                        .credentialId(credential.getId())
                        .address(credential.getAddress())
                        .phone(credential.getPhone())
                        .image(credential.getImage())
                        .name(credential.getFactory().getName())
                        .location(credential.getFactory().getLocation())
                        .isCollaborating(credential.getFactory().isCollaborating()).build()).collect(Collectors.toList());

        Page<FactoryPageResponseDto> pageReturn = new PageImpl(credentialFactories,pageable,credentialFactories.size());
        return pageReturn;
    }

    @Override
    public AddFactoryResponse addFactory(AddFactoryDto factoryDto) {
        Optional<Credential> credentialOptional = credentialRepository.findCredentialByEmail(factoryDto.getEmail());
        if (credentialOptional.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
        }
        Factory factory = Factory.builder()
                .isCollaborating(true)
                .name(factoryDto.getName())
                .location(factoryDto.getAddress())
                .build();
        Role role = roleRepository.findByName(RoleName.ROLE_FACTORY)
                .orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                .email(factoryDto.getEmail())
                .address(factoryDto.getAddress())
                .phone(factoryDto.getPhone())
                .role(role)
                .image(factoryDto.getLogo())
                .factory(factory)
                .password(passwordEncoder.encode(factoryDto.getPassword())).build();
        factoryRepository.save(factory);
        Credential credentialInrepo = credentialRepository.save(credential);
        AddFactoryResponse addFactoryDto = modelMapper.map(credentialInrepo, AddFactoryResponse.class);
        return addFactoryDto;
    }

    @Override
    public FactoryByIdDto getFactorybyCredentialId(String credentialId) {
        Credential credential = credentialRepository.findById(credentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        if(credential.getFactory() != null){
            List<PriceByFactory>  priceByFactories = (List<PriceByFactory>) credential.getFactory().getPriceByFactories();
            Set<ProductDto> productDtoList = new HashSet<>();
            for (int i = 0; i < priceByFactories.size(); i++) {
               ProductDto productDto = ProductDto.builder()
                       .id(priceByFactories.get(i).getProduct().getId())
                       .material(priceByFactories.get(i).getMaterial())
                       .price(priceByFactories.get(i).getPrice())
                       .name(priceByFactories.get(i).getProduct().getName())
                       .createDate(priceByFactories.get(i).getProduct().getCreateDate())
                       .isPublic(priceByFactories.get(i).getProduct().isPublic())
                       .isDeleted(priceByFactories.get(i).getProduct().isDeleted())
                       .productImages(priceByFactories.get(i).getProduct().getProductImages().stream().map(productImages -> ProductImagesDto.builder().image(productImages.getImage()).build()).collect(Collectors.toList()))
                       .categoryName(priceByFactories.get(i).getProduct().getCategory().getName())
                       .sizeColors(sizeColorByFactoryRepository.findAllBySizeColorProductId(priceByFactories.get(i).getProduct().getId()).stream().filter(sizeColorByFactory -> sizeColorByFactory.getFactory().getId().equals(credential.getFactory().getId())).map(sizeColorByFactory -> SizeColorInFactoryDetailDto.builder().size(sizeColorByFactory.getSizeColor().getSize().getName()).colorImage(sizeColorByFactory.getSizeColor().getColor().getName()).quantity(sizeColorByFactory.getQuantity()).build()).collect(Collectors.toSet()))
                       .build();
               productDtoList.add(productDto);
            }
            List<ProductDto> productDtos = productDtoList.stream().sorted(Comparator.comparing(productDto -> productDto.getCreateDate())).distinct().collect(Collectors.toList());
        FactoryByIdDto factory = FactoryByIdDto.builder().id(credential.getFactory().getId())
                        .email(credential.getEmail())
                        .name(credential.getFactory()
                        .getName()).location(credential.getFactory()
                        .getLocation()).phone(credential.getPhone())
                        .address(credential.getAddress())
                        .image(credential.getImage())
                        .productDtoList(productDtos)
                        .isCollaborating(credential.getFactory().isCollaborating()).build();
        return factory;
        }
        throw new UserNameExistException(UserErrorMessage.FACTORY_NOT_EXIST_EXCETPTION);
    }

    @Override
    public UserDto updatePassword(UpdatePasswordDto user, String credentialId) {
        Credential userRepo =  getPermittedCredential(credentialId);
        if(!passwordEncoder.matches(user.getOldPassword(),userRepo.getPassword())){
            throw new PasswordNotMatchException(UserErrorMessage.PASSWORD_DOES_NOT_MATCH);
        }
        userRepo.setPassword(passwordEncoder.encode(user.getNewPassword()));
        return modelMapper.map(credentialRepository.save(userRepo),UserDto.class);
    }
    @Override
    public UserDto updateAvatar(UpdateAvatarDto avatar, String userId) {
        Credential credential =  getPermittedCredential(userId);
        credential.setImage(avatar.getImage());
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }

    @Override
    public void updateCollaborating(String factoryId, boolean isCollaborating) {
      Factory factory =  factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
      factory.setCollaborating(isCollaborating);
      factoryRepository.save(factory);
    }

    @Override
    @Transactional
    public void addSizeColorToProduct(String factoryId, String productId, List<SizeColorInFactoryDetailDto> sizeColors) {
        Product product =  productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        Factory factory =  factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
        Set<SizeColorByFactory> sizeColorByFactories = new HashSet<>();
        for (int i = 0; i < sizeColors.size(); i++) {
            SizeColor sizeColor = sizeColorRepository.findByColorImageColorAndSizeNameAndProductId(sizeColors.get(i).getColorImage(),sizeColors.get(i).getSize(), product.getId())
                    .orElseThrow(() -> new SizeNotFoundException(SizeColorErrorMessage.SIZE_AND_COLOR_NOT_EXIST_EXCEPTION));
            Optional<SizeColorByFactory> sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryIdAndSizeColorId(factoryId,sizeColor.getId());
            if(sizeColorByFactory.isPresent()){
               sizeColorByFactory.get().setQuantity(sizeColorByFactory.get().getQuantity()+sizeColors.get(i).getQuantity());
               sizeColorByFactories.add(sizeColorByFactory.get());
            }
            else {
            SizeColorByFactory sizeColorByFactoryAdd = SizeColorByFactory.builder().factory(factory).sizeColor(sizeColor).quantity(sizeColors.get(i).getQuantity()).build();
            sizeColorByFactories.add(sizeColorByFactoryAdd);
            }
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
    }
    @Override
    public void addPriceByFactoryToProduct(String factoryId, String productId, double price, String material) {
       Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
       Factory factory = factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
       Optional<PriceByFactory> priceByFactoryInRepo =  priceByFactoryRepository.getByProductIdAndFactoryId(productId, factoryId);
       if(priceByFactoryInRepo.isPresent()){
           throw new PriceByFactoryExistedException(ProductErrorMessage.PRICE_BY_FACTORY_EXISTED);
       }
       PriceByFactory priceByFactory = PriceByFactory.builder().factory(factory).product(product).price(price).material(material).build();
       priceByFactoryRepository.save(priceByFactory);
    }
    @Override
    public void updatePriceByFactoryToProduct(String factoryId, String productId, double price, String material) {
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
        PriceByFactory priceByFactoryInRepo =  priceByFactoryRepository.getByProductIdAndFactoryId(productId, factoryId).orElseThrow(() -> new PriceByFactoryNotExistedException(ProductErrorMessage.PRICE_BY_FACTORY_NOT_EXISTED));
        priceByFactoryInRepo.setPrice(price);
        priceByFactoryInRepo.setMaterial(material);
        priceByFactoryRepository.save(priceByFactoryInRepo);
    }
    @Override
    public List<OrderDetailFactoryDto> getAllOrderDetailsForFactoryByCredentialId(String credentialId) {
        Optional<Credential> credential = credentialRepository.findById(credentialId);
        getPermittedCredential(credentialId);
        if(credential.isPresent()){
            if(credential.get().getFactory()!=null){
                List<OrderDetail> orderDetailList= orderDetailRepository.findAllByFactoryId(credential.get().getFactory().getId());
                List<OrderDetailFactoryDto> orderDetailFactoryDtos = orderDetailList.stream().filter(orderDetail ->orderDetail.getOrders().isPaid()==true).map(orderDetail -> OrderDetailFactoryDto.builder()
                       .id(orderDetail.getId())
                       .orderId(orderDetail.getOrders().getId())
                       .designId(orderDetail.getDesignedProduct().getId())
                       .productName(orderDetail.getDesignedProduct().getProduct().getName())
                       .designName(orderDetail.getDesignedProduct().getName())
                       .designedImage(orderDetail.getDesignedProduct().getImagePreviews().stream().collect(Collectors.toList()).get(0).getImage())
                       .color(orderDetail.getColor())
                       .size(orderDetail.getSize())
                       .price((orderDetail.getDesignedProduct().getPriceByFactory().getPrice() + orderDetail.getDesignedProduct().getDesignedPrice() ) * orderDetail.getQuantity())
                       .quantity(orderDetail.getQuantity())
                       .status(orderStatusRepository.findAllByOrderDetailId(orderDetail.getId()).stream().sorted().map(orderStatus -> orderStatus.getName()).collect(Collectors.toList()).get(0))
                       .build()).collect(Collectors.toList());
               return orderDetailFactoryDtos;
            }
        }
        return null;
    }

    @Override
    public OrderDetailForPrintingDto getAllOrderDetailsForPrintingByOrderDetailsId(String orderId, String designId, String credentialId) {
        Optional<Credential> credential = credentialRepository.findById(credentialId);

        if(credential.isPresent()) {
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrdersIdAndDesignedProductIdAndFactoryId(orderId, designId, credential.get().getFactory().getId());
            if (!orderDetails.isEmpty()) {
                OrderDetailForPrintingDto orderDetailForPrintingDto = OrderDetailForPrintingDto.builder()
                        .orderId(orderDetails.get(0).getOrders().getId())
                        .createDate(orderDetails.get(0).getOrders().getCreateDate().toString())
                        .productId(orderDetails.get(0).getDesignedProduct().getProduct().getId())
                        .bluePrintDtos(orderDetails.get(0).getPrintingInfo().getPrintingBluePrints().stream().map(bluePrint -> modelMapper.map(bluePrint, BluePrintDto.class)).collect(Collectors.toList()))
                        .status(orderDetails.get(0).getOrderStatuses().stream().sorted().collect(Collectors.toList()).get(0).getName())
                        .build();
                List<OrderDetailsSupportDto> orderDetailsSupportDtos = new ArrayList<>();
                List<ImagePreviewDto> imagePreviewDtos = new ArrayList<>();

                for (int i = 0; i < orderDetails.size(); i++) {
                    orderDetailsSupportDtos.add(OrderDetailsSupportDto.builder().orderDetailsId(orderDetails.get(i).getId())
                            .color(orderDetails.get(i).getColor())
                            .colorImage(colorRepository.findByName(orderDetails.get(i).getColor()).get().getImageColor())
                            .size(orderDetails.get(i).getSize())
                            .quantity(orderDetails.get(i).getQuantity())
                            .build());
                    for (int j = 0; j < orderDetails.get(i).getPrintingInfo().getPreviewImages().size(); j++) {
                        imagePreviewDtos.add(ImagePreviewDto.builder()
                                .image(orderDetails.get(i).getPrintingInfo().getPreviewImages().get(j).getImage())
                                .color(orderDetails.get(i).getPrintingInfo().getPreviewImages().get(j).getColor())
                                .position(orderDetails.get(i).getPrintingInfo().getPreviewImages().get(j).getPosition())
                                .build());
                    }
                }
                orderDetailForPrintingDto.setCustomerName(orderDetails.get(0).getOrders().getCustomerName());
                orderDetailForPrintingDto.setEmail(orderDetails.get(0).getOrders().getUser().getCredential().getEmail());
                orderDetailForPrintingDto.setPhoneNumber(orderDetails.get(0).getOrders().getPhone());
                orderDetailForPrintingDto.setAddress(orderDetails.get(0).getOrders().getAddress());
                orderDetailForPrintingDto.setOrderDetailsSupportDtos(orderDetailsSupportDtos);
                orderDetailForPrintingDto.setPreviewImages(imagePreviewDtos);
                return orderDetailForPrintingDto;
            }
            else throw new OrderNotFoundException(FactoryErrorMessage.FACTORY_NOT_HAVE_THIS_ORDER);
        }
        return null;
    }
}
