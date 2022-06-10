package com.capstone.pod.services.implement;

import com.capstone.pod.constant.category.CategoryErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.converter.FactoryConverter;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.factory.FactoryProductDetailDTO;
import com.capstone.pod.dto.product.*;
import com.capstone.pod.dto.tag.TagDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CategoryNotFoundException;
import com.capstone.pod.exceptions.ProductNameExistException;
import com.capstone.pod.exceptions.ProductNotFoundException;
import com.capstone.pod.repositories.CategoryRepository;
import com.capstone.pod.repositories.ProductRepository;
import com.capstone.pod.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    private final FactoryConverter factoryConverter;

    @Override
    public ProductDto addProduct(AddProductDto productDto) {
        Optional<Product> productInRepo = productRepository.findByName(productDto.getName());
        if(productInRepo.isPresent()){
            throw new ProductNameExistException(ProductErrorMessage.PRODUCT_NAME_EXISTED);
        }
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_NAME_NOT_FOUND));
        Product product = Product.builder().name(productDto.getName())
                .description(productDto.getDescription()).category(category).isDeleted(false).isPublic(false).build();
        List<ProductImages> imagesList = new ArrayList<>();
        for (int i = 0; i < productDto.getImages().size(); i++) {
            imagesList.add(ProductImages.builder().product(product).image(productDto.getImages().get(i)).build());
        }
        product.setProductImages(imagesList);
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }
    @Override
    public ProductDto updateProduct(UpdateProductDto productDto,int productId) {
        Product productInRepo = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_NAME_NOT_FOUND));
        List<ProductImages> imagesList = new ArrayList<>();
        for (int i = 0; i < productDto.getImages().size(); i++) {
            imagesList.add(ProductImages.builder().product(productInRepo).image(productDto.getImages().get(i)).build());
        }
        productInRepo.setProductImages(imagesList);
        productInRepo.setName(productDto.getName());
        productInRepo.setCategory(category);
        productInRepo.setDescription(productDto.getDescription());
        return modelMapper.map(productRepository.save(productInRepo),ProductDto.class);
    }
    @Override
    public ProductDto publishProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        product.setPublic(true);
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }

    @Override
    public PageDTO getAllProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        List<GetAllProductDto> dtos = new ArrayList<>();
        for(var product : pageProduct){
            GetAllProductDto productDto = GetAllProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryName(product.getCategory().getName())
                .numberOfColor(product.getSizeColors().stream().map(SizeColor::getColor).distinct().collect(Collectors.toList()).size())
                .numberOfSize(product.getSizeColors().stream().map(SizeColor::getSize).distinct().collect(Collectors.toList()).size())
                .build();
            productDto.setProductImages(product.getProductImages().stream().map(img -> ProductImagesDto.builder().image(img.getImage()).build()).collect(Collectors.toList()));
            productDto.setTags(product.getProductTags().stream().map(tag -> ProductTagDto.builder().tag(TagDto.builder().name(tag.getTag().getName()).build()).build()).collect(Collectors.toList()));
            dtos.add(productDto);
        }
        return PageDTO.builder()
            .data(dtos)
            .elements((int)pageProduct.getTotalElements())
            .page(pageProduct.getPageable().getPageNumber())
            .build();

    }
    @Override
    public Page<ProductDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        Page<ProductDto> pageProductDTO = pageProduct.map(product -> modelMapper.map(product, ProductDto.class));
        return pageProductDTO;

    }

    @Override
    public ProductDto deleteProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        product.setDeleted(true);
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }

    @Override
    public ProductDetailDto getProductById(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        if (!product.isPublic() || product.isDeleted()) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST);
        }
        List<SizeColorByFactory> sizeColorByFactoryList = new ArrayList<>();
        for(var sizeColor : product.getSizeColors()){
            for(var sizeColorByFactory : sizeColor.getSizeColorByFactories()){
                sizeColorByFactoryList.add(sizeColorByFactory);
            }
        }

        Map<Factory , List<SizeColorByFactory>> groupSizeColorByFactory = sizeColorByFactoryList.stream()
            .collect(groupingBy(SizeColorByFactory::getFactory));

        Map<Factory , List<PriceByFactory>> groupPriceByFactory = product.getPriceByFactories().stream()
            .collect(groupingBy(PriceByFactory::getFactory));
        List<FactoryProductDetailDTO> factories = new ArrayList<>();
        for(var factoryEntry  : groupSizeColorByFactory.entrySet()){
            var factory  = factoryEntry.getKey();
            FactoryProductDetailDTO factoryProductDetailDTO =
                FactoryProductDetailDTO.builder()
                    .id(factory.getId())
                    .name(factory.getName())
                    .location(factory.getLocation())
                    .price(groupPriceByFactory.get(factoryEntry.getKey()).isEmpty() ? 0 : groupPriceByFactory.get(factoryEntry.getKey()).get(0).getPrice())
                    .area(product.getProductBluePrints().stream().map(ProductBluePrint::getPosition).collect(Collectors.toList()))
                    .sizes(groupSizeColorByFactory.get(factoryEntry.getKey())
                        .stream().map(sizeColor  -> sizeColor.getSizeColor().getSize().getName())
                        .collect(Collectors.toList()))
                    .colors(groupSizeColorByFactory.get(factoryEntry.getKey())
                        .stream().map(sizeColor  -> sizeColor.getSizeColor().getColor().getName())
                        .collect(Collectors.toList()))
                    .build();
            factories.add(factoryProductDetailDTO);
        }
        return ProductDetailDto.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .images(product.getProductImages().stream().map(ProductImages::getImage).collect(Collectors.toList()))
            .tags(product.getProductTags().stream().map(t -> t.getTag().getName()).collect(Collectors.toList()))
            .factories(
                factories)
            .build();
    }

    @Override
    public ProductDto getProductByIdAdmin(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        return modelMapper.map(product,ProductDto.class);
    }
}
