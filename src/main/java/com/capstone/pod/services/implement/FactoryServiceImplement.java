package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.factory.FactoryErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.factory.FactoryByIdDto;
import com.capstone.pod.dto.factory.FactoryPageResponseDto;
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

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private  Credential getPermittedCredential(int credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        if(!currentCredentialId.equals(credential.getId())){
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        return credential;
    }

    @Override
    public Page<FactoryPageResponseDto> getAllFactories(Pageable pageable) {
        List<FactoryPageResponseDto> credentialFactories= credentialRepository.findAll(pageable).stream().filter(credential -> credential.getRole().getName().equals(RoleName.ROLE_FACTORY))
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
                .build();
        Role role = roleRepository.findByName(RoleName.ROLE_FACTORY)
                .orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                .email(factoryDto.getEmail())
                .address(factoryDto.getAddress())
                .phone(factoryDto.getPhone())
                .role(role)
                .factory(factory)
                .password(passwordEncoder.encode(factoryDto.getPassword())).build();
        factoryRepository.save(factory);
        Credential credentialInrepo = credentialRepository.save(credential);
        AddFactoryResponse addFactoryDto = modelMapper.map(credentialInrepo, AddFactoryResponse.class);
        return addFactoryDto;
    }

    @Override
    public FactoryByIdDto getFactorybyCredentialId(int credentialId) {
        Credential credential = credentialRepository.findById(credentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        if(credential.getFactory() != null){
            List<SizeColorByFactory>  sizeColorByFactories = (List<SizeColorByFactory>) credential.getFactory().getSizeColorByFactories();
            Set<Product> productList = new HashSet<>();
            for (int i = 0; i < sizeColorByFactories.size(); i++) {
               productList.add(sizeColorByFactories.get(i).getSizeColor().getProduct());
            }
            List<ProductDto> productDtoList = productList.stream().map(product -> ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .isPublic(product.isPublic())
                    .isDeleted(product.isDeleted())
                    .productImages(product.getProductImages().stream().map(productImages -> ProductImagesDto.builder().image(productImages.getImage()).build()).collect(Collectors.toList()))
                    .categoryName(product.getCategory().getName())
                    .sizeColors(sizeColorByFactoryRepository.findAllBySizeColorProductId(product.getId()).stream().map(sizeColorByFactory -> SizeColorInFactoryDetailDto.builder().size(sizeColorByFactory.getSizeColor().getSize().getName()).color(sizeColorByFactory.getSizeColor().getColor().getName()).quantity(sizeColorByFactory.getQuantity()).build()).collect(Collectors.toSet()))
                    .build()).sorted(Comparator.comparing(productDto -> productDto.getId())).collect(Collectors.toList());
        FactoryByIdDto factory = FactoryByIdDto.builder().id(credential.getFactory().getId())
                        .email(credential.getEmail())
                        .name(credential.getFactory()
                        .getName()).location(credential.getFactory()
                        .getLocation()).phone(credential.getPhone())
                        .address(credential.getAddress())
                        .image(credential.getImage())
                        .productDtoList(productDtoList)
                        .isCollaborating(credential.getFactory().isCollaborating()).build();
        return factory;
        }
        throw new UserNameExistException(UserErrorMessage.FACTORY_NOT_EXIST_EXCETPTION);
    }

    @Override
    public UserDto updatePassword(UpdatePasswordDto user, int credentialId) {
        Credential userRepo =  getPermittedCredential(credentialId);
        if(!passwordEncoder.matches(user.getOldPassword(),userRepo.getPassword())){
            throw new PasswordNotMatchException(UserErrorMessage.PASSWORD_DOES_NOT_MATCH);
        }
        userRepo.setPassword(passwordEncoder.encode(user.getNewPassword()));
        return modelMapper.map(credentialRepository.save(userRepo),UserDto.class);
    }
    @Override
    public UserDto updateAvatar(UpdateAvatarDto avatar, int userId) {
        Credential credential =  getPermittedCredential(userId);
        credential.setImage(avatar.getImage());
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }

    @Override
    public void updateCollaborating(int factoryId, boolean isCollaborating) {
      Factory factory =  factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
      factory.setCollaborating(isCollaborating);
      factoryRepository.save(factory);
    }

    @Override
    public void addSizeColorToProduct(int factoryId, int productId, List<SizeColorInFactoryDetailDto> sizeColors) {
        Product product =  productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        Factory factory =  factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
        Set<SizeColorByFactory> sizeColorByFactories = new HashSet<>();
        for (int i = 0; i < sizeColors.size(); i++) {
            SizeColor sizeColor = sizeColorRepository.findByColorNameAndSizeNameAndProductId(sizeColors.get(i).getColor(),sizeColors.get(i).getSize(), product.getId())
                    .orElseThrow(() -> new SizeNotFoundException(SizeColorErrorMessage.SIZE_AND_COLOR_NOT_EXIST_EXCEPTION));
            Optional<SizeColorByFactory> sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryIdAndSizeColorId(factoryId,sizeColor.getId());
            if(sizeColorByFactory.isPresent()){
                throw new SizeExistedException(SizeColorErrorMessage.SIZE_AND_COLOR_EXISTED_IN_FACTORY_EXCEPTION);
            }
            SizeColorByFactory sizeColorByFactoryAdd = SizeColorByFactory.builder().factory(factory).sizeColor(sizeColor).quantity(sizeColors.get(i).getQuantity()).build();
            sizeColorByFactories.add(sizeColorByFactoryAdd);
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
    }

    @Override
    public void addPriceByFactoryToProduct(int factoryId, int productId, double price) {
       Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
       Factory factory = factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
       Optional<PriceByFactory> priceByFactoryInRepo =  priceByFactoryRepository.getByProductIdAndFactoryId(productId, factoryId);
       if(priceByFactoryInRepo.isPresent()){
           throw new PriceByFactoryExistedException(ProductErrorMessage.PRICE_BY_FACTORY_EXISTED);
       }
       PriceByFactory priceByFactory = PriceByFactory.builder().factory(factory).product(product).price(price).build();
       priceByFactoryRepository.save(priceByFactory);
    }

    @Override
    public void updatePriceByFactoryToProduct(int factoryId, int productId, double price) {
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        factoryRepository.findById(factoryId).orElseThrow(() -> new FactoryNotFoundException(FactoryErrorMessage.FACTORY_NOT_FOUND));
        PriceByFactory priceByFactoryInRepo =  priceByFactoryRepository.getByProductIdAndFactoryId(productId, factoryId).orElseThrow(() -> new PriceByFactoryNotExistedException(ProductErrorMessage.PRICE_BY_FACTORY_NOT_EXISTED));
        priceByFactoryInRepo.setPrice(price);
        priceByFactoryRepository.save(priceByFactoryInRepo);
    }
}
