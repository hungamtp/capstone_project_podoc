package com.capstone.pod.services.implement;

import com.capstone.pod.constant.category.CategoryErrorMessage;
import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.converter.FactoryConverter;
import com.capstone.pod.dto.color.ColorInDesignDto;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.factory.FactoryProductDetailDto;
import com.capstone.pod.dto.placeholder.PlaceHolderDto;
import com.capstone.pod.dto.product.*;
import com.capstone.pod.dto.sizecolor.SizeColorByProductIdDto;
import com.capstone.pod.dto.sizecolor.SizeColorInRawProductDto;
import com.capstone.pod.dto.tag.TagDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CategoryNotFoundException;
import com.capstone.pod.exceptions.PermissionException;
import com.capstone.pod.exceptions.ProductNameExistException;
import com.capstone.pod.exceptions.ProductNotFoundException;
import com.capstone.pod.repositories.*;
import com.capstone.pod.repositories.impl.projection.FactoryRateProjection;
import com.capstone.pod.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PriceByFactoryRepository priceByFactoryRepository;
    private final ModelMapper modelMapper;
    private final ProductBluePrintRepository productBluePrintRepository;
    private final SizeColorRepository sizeColorRepository;
    private final OrdersRepository ordersRepository;

    private final ProductImagesRepository productImagesRepository;
    private final FactoryConverter factoryConverter;

    @Override
    public ProductDto addProduct(AddProductDto productDto) {
        Optional<Product> productInRepo = productRepository.findByName(productDto.getName());
        if (productInRepo.isPresent()) {
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
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductReturnDto updateProduct(UpdateProductDto productDto, String productId) {
        Product productInRepo = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        if (productInRepo.isDeleted()) throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_DELETED);
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_NAME_NOT_FOUND));
        productImagesRepository.deleteAllInBatch(productInRepo.getProductImages());
        List<ProductImages> imagesList = new ArrayList<>();
        for (int i = 0; i < productDto.getImages().size(); i++) {
            imagesList.add(ProductImages.builder().product(productInRepo).image(productDto.getImages().get(i)).build());
        }
        productInRepo.setProductImages(imagesList);
        productInRepo.setName(productDto.getName());
        productInRepo.setCategory(category);
        productInRepo.setDescription(productDto.getDescription());
        return modelMapper.map(productRepository.save(productInRepo), ProductReturnDto.class);
    }

    @Override
    public ProductReturnDto publishProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        if (product.getSizeColors().isEmpty()) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_HAVE_COLOR_AND_SIZE);
        }
        if (product.getSizeColors().stream().flatMap(sizeColor -> Stream.of(sizeColor.getSizeColorByFactories())).collect(Collectors.toList()).size() == 0) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_HAVE_COLOR_AND_SIZE);
        }
        boolean check = false;
        for (int i = 0; i < product.getSizeProduct().size(); i++) {
            if(product.getSizeProduct().get(i).getSize().equalsIgnoreCase("L")) check = true;
        }
        if(!check) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_HAVE_SIZE_L);
        }
        if (product.getPriceByFactories().isEmpty()) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_HAVE_PRICE);
        }
        if (product.getProductBluePrints().isEmpty()) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_HAVE_NO_BLUEPRINT);
        }
        product.setPublic(true);
        return modelMapper.map(productRepository.save(product), ProductReturnDto.class);
    }

    @Override
    public ProductReturnDto unPublishProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        product.setPublic(false);
        return modelMapper.map(productRepository.save(product), ProductReturnDto.class);
    }

    @Override
    public PageDTO getAllProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        List<GetAllProductDto> dtos = new ArrayList<>();
        for (var product : pageProduct) {
            var priceList = product.getPriceByFactories();
            List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
            for (var sizeColor : product.getSizeColors()) {
                sizeColorByFactories.addAll(sizeColor.getSizeColorByFactories());
            }

            GetAllProductDto productDto = GetAllProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryName(product.getCategory().getName())
                .numberOfColor(Long.valueOf(sizeColorByFactories.stream().map(SizeColorByFactory::getSizeColor).distinct().collect(Collectors.toList()).stream().map(SizeColor::getColor).distinct().count()).intValue())
                .numberOfSize(Long.valueOf(sizeColorByFactories.stream().map(SizeColorByFactory::getSizeColor).distinct().collect(Collectors.toList()).stream().map(SizeColor::getSize).distinct().count()).intValue())
                .priceFrom(priceList.size() == 0 ? 0 : priceList.stream().min(Comparator.comparingDouble(PriceByFactory::getPrice)).get().getPrice())
                .numberOfFactory(product.getPriceByFactories().stream().filter(priceByFactory -> priceByFactory.getFactory().isCollaborating()).collect(Collectors.toList()).size())
                .build();
            productDto.setProductImages(product.getProductImages().stream().map(img -> ProductImagesDto.builder().image(img.getImage()).build()).collect(Collectors.toList()));
            productDto.setTags(product.getProductTags().stream().map(tag -> ProductTagDto.builder().tag(TagDto.builder().name(tag.getTag().getName()).build()).build()).collect(Collectors.toList()));
            dtos.add(productDto);
        }
        return PageDTO.builder()
            .data(dtos)
            .elements((int) pageProduct.getTotalElements())
            .page(pageProduct.getPageable().getPageNumber())
            .build();

    }

    @Override
    public Page<ProductByAdminDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        Page<ProductByAdminDto> pageProductDTO = pageProduct.map(product -> modelMapper.map(product, ProductByAdminDto.class));
        return pageProductDTO;
    }

    @Override
    public ProductReturnDto deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        product.setDeleted(true);
        return modelMapper.map(productRepository.save(product), ProductReturnDto.class);
    }

    @Override
    public ProductDetailDto getProductById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        if (!product.isPublic() || product.isDeleted()) {
            throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST);
        }

        List<Rating> rates = product.getDesignedProducts().stream()
            .flatMap(designedProduct -> designedProduct.getRatings().stream())
            .collect(Collectors.toList());

        OptionalDouble rateProduct = rates.stream().mapToDouble(Rating::getRatingStar).average();

        List<SizeColorByFactory> sizeColorByFactoryList = new ArrayList<>();
        for (var sizeColor : product.getSizeColors()) {
            for (var sizeColorByFactory : sizeColor.getSizeColorByFactories()) {
                sizeColorByFactoryList.add(sizeColorByFactory);
            }
        }
        Map<Factory, List<SizeColorByFactory>> groupSizeColorByFactory = sizeColorByFactoryList.stream()
            .collect(groupingBy(SizeColorByFactory::getFactory));

        Map<Factory, List<PriceByFactory>> groupPriceByFactory = product.getPriceByFactories().stream()
            .collect(groupingBy(PriceByFactory::getFactory));
        List<FactoryProductDetailDto> factories = new ArrayList<>();
        Double highestPrice = Double.MIN_VALUE;
        Double lowestPrice = Double.MAX_VALUE;
        for (var factoryEntry : groupSizeColorByFactory.entrySet()) {
            var factory = factoryEntry.getKey();
            var factoryPrice = product.getPriceByFactories().stream()
                .filter(f -> f.getFactory().getId() == factoryEntry.getKey().getId()).collect(Collectors.toList());
            Double price = factoryPrice.size() == 0 ? 0 : factoryPrice.get(0).getPrice();
            if (price > highestPrice) {
                highestPrice = price;
            }
            if (price < lowestPrice) {
                lowestPrice = price;
            }
            FactoryRateProjection factoryRateProjection = ordersRepository.getRateFactory(factory.getId());
            FactoryProductDetailDto factoryProductDetailDTO =
                FactoryProductDetailDto.builder()
                    .id(factory.getId())
                    .name(factory.getName())
                        .isCollaborating(factory.isCollaborating())
                    .location(factory.getLocation())
                    .rate(factoryRateProjection.getRates() == null ? 0 : Double.valueOf(factoryRateProjection.getRates()).floatValue())
                    .rateCount(factoryRateProjection.getCount() == null ? 0 : factoryRateProjection.getCount())
                    .material((groupPriceByFactory.get(factoryEntry.getKey()) != null && groupPriceByFactory.get(factoryEntry.getKey()).isEmpty()) ?
                        "" : groupPriceByFactory.get(factoryEntry.getKey()).get(0).getMaterial())
                    .price(
                        (groupPriceByFactory.get(factoryEntry.getKey()) != null && groupPriceByFactory.get(factoryEntry.getKey()).isEmpty()) ?
                            0 : groupPriceByFactory.get(factoryEntry.getKey()).get(0).getPrice())
                    .area(product.getProductBluePrints().stream().map(ProductBluePrint::getPosition).collect(Collectors.toList()))
                    .sizes(groupSizeColorByFactory.get(factoryEntry.getKey())
                        .stream().map(sizeColor -> sizeColor.getSizeColor().getSize().getName())
                        .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList()))
                    .colors(groupSizeColorByFactory.get(factoryEntry.getKey())
                        .stream().distinct().map(sizeColor -> sizeColor.getSizeColor().getColor().getImageColor())
                        .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList()))
                    .build();
            if(factoryProductDetailDTO.isCollaborating()){
            factories.add(factoryProductDetailDTO);}
        }
        return ProductDetailDto.builder()
            .id(product.getId())
            .name(product.getName())
            .rate(rateProduct.isPresent() ? Double.valueOf(rateProduct.getAsDouble()).floatValue() : 0)
            .rateCount(rates.size())
            .description(product.getDescription())
            .images(product.getProductImages().stream().map(ProductImages::getImage).collect(Collectors.toList()))
            .tags(product.getProductTags().stream().map(t -> t.getTag().getName()).collect(Collectors.toList()))
            .lowestPrice(lowestPrice)
            .highestPrice(highestPrice)
            .factories(
                factories)
            .build();
    }

    @Override
    public ProductByAdminDto getProductByIdAdmin(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        return modelMapper.map(product, ProductByAdminDto.class);
    }

    @Override
    public List<ProductBluePrintDto> getProductBluePrintByProductId(String productId) {
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        List<ProductBluePrint> productBluePrintDtos = productBluePrintRepository.getAllByProductId(productId);
        return productBluePrintDtos.stream().map(blueprint -> ProductBluePrintDto.builder().id(blueprint.getId()).frameImage(blueprint.getFrameImage()).position(blueprint.getPosition()).placeholder(PlaceHolderDto.builder().top(blueprint.getPlaceHolderTop()).widthRate(blueprint.getWidthRate()).heightRate(blueprint.getHeightRate()).height(blueprint.getPlaceHolderHeight()).width(blueprint.getPlaceHolderWidth()).build()).build()).collect(Collectors.toList());
    }

    @Override
    public SizeColorByProductIdDto getSizesAndColorByProductId(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        List<ColorInDesignDto> colors = product.getSizeColors().stream().map(sizeColor -> ColorInDesignDto.builder().name(sizeColor.getColor().getName()).image(sizeColor.getColor().getImageColor()).id(sizeColor.getColor().getId()).build()).distinct().collect(Collectors.toList());
        List<String> sizes = product.getSizeColors().stream().map(sizeColor -> sizeColor.getSize().getName()).distinct().collect(Collectors.toList());
        return SizeColorByProductIdDto.builder().colors(colors).sizes(sizes).build();
    }

    @Override
    public List<ColorInDesignDto> getColorsByProductNameAndFactoryName(String productId, String factoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        List<ColorInDesignDto> colors = new ArrayList();
        for (int i = 0; i < product.getSizeColors().size(); i++) {
            List<ColorInDesignDto> colorTmp = product.getSizeColors().get(i)
                .getSizeColorByFactories().stream().filter(sizeColorByFactory -> sizeColorByFactory.getFactory().getId().equals(factoryId))
                .map(sizeColorByFactory -> ColorInDesignDto.builder().id(sizeColorByFactory.getSizeColor().getColor().getId()).name(sizeColorByFactory.getSizeColor().getColor().getName()).image(sizeColorByFactory.getSizeColor().getColor().getImageColor()).build()).collect(Collectors.toList());
            colors.addAll(colorTmp);
        }
        List<ColorInDesignDto> colorsReturn = colors.stream().distinct().collect(Collectors.toList());
        return colorsReturn;
    }

    @Override
    public List<GetProductFactoryDto> getAllProductForFactoryDoNotHaveYet(String factoryId, String productName) {
        List<PriceByFactory> priceByFactoriesAll = priceByFactoryRepository.findAll();
        List<PriceByFactory> priceByFactoriesInFactory = priceByFactoriesAll.stream().filter(priceByFactory -> priceByFactory.getFactory().getId().equals(factoryId)).collect(Collectors.toList());
        List<Product> productsAll = productRepository.findAll();
        List<Product> productsInFactory = priceByFactoriesInFactory.stream().map(priceByFactory -> priceByFactory.getProduct()).distinct().collect(Collectors.toList());
        List<Product> productsReturn = new ArrayList<>();
        boolean tmp = true;
        for (int i = 0; i < productsAll.size(); i++) {
            for (int j = 0; j < productsInFactory.size(); j++) {
                if (productsAll.get(i).equals(productsInFactory.get(j))) {
                    tmp = false;
                }
            }
            if (tmp) {
                productsReturn.add(productsAll.get(i));
            }
            tmp = true;
        }
        if(!productName.isEmpty()) {
            return productsReturn.stream().map(product -> GetProductFactoryDto.builder().id(product.getId()).name(product.getName()).build()).filter(getProductFactoryDto -> getProductFactoryDto.getName().toLowerCase().contains(productName.toLowerCase())).collect(Collectors.toList());
        }
        return productsReturn.stream().map(product -> GetProductFactoryDto.builder().id(product.getId()).name(product.getName()).build()).collect(Collectors.toList());
        }

    @Override
    public AddProductBluePrintDto addProductBluePrint(String productId, AddProductBluePrintDto addProductBluePrintDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        List<ProductBluePrint> productBluePrintList = productBluePrintRepository.getAllByProductId(productId);
        for (int i = 0; i < productBluePrintList.size(); i++) {
            if (productBluePrintList.get(i).getPosition().equals(addProductBluePrintDto.getPosition())) {
                throw new PermissionException(CommonMessage.POSITION_EXCEPTION);
            }
        }
        ProductBluePrint productBluePrint = ProductBluePrint.builder()
            .product(product)
            .widthRate(addProductBluePrintDto.getWidthRate())
            .heightRate(addProductBluePrintDto.getHeightRate())
            .frameImage(addProductBluePrintDto.getFrameImage())
            .position(addProductBluePrintDto.getPosition())
            .placeHolderTop(addProductBluePrintDto.getPlaceHolderTop())
            .placeHolderHeight(addProductBluePrintDto.getPlaceHolderHeight())
            .placeHolderWidth(addProductBluePrintDto.getPlaceHolderWidth())
            .build();
        return modelMapper.map(productBluePrintRepository.save(productBluePrint), AddProductBluePrintDto.class);
    }

    @Override
    public EditProductBluePrintDto editProductBluePrint(String productBlueprintId, EditProductBluePrintDto dto) {
        ProductBluePrint productBluePrint = productBluePrintRepository.findById(productBlueprintId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.BLUEPRINT_NOT_FOUND));
        productBluePrint.setFrameImage(dto.getFrameImage());
        productBluePrint.setPlaceHolderTop(dto.getPlaceHolderTop());
        productBluePrint.setWidthRate(dto.getWidthRate());
        productBluePrint.setHeightRate(dto.getHeightRate());
        productBluePrint.setPlaceHolderHeight(dto.getPlaceHolderHeight());
        productBluePrint.setPlaceHolderWidth(dto.getPlaceHolderWidth());
        return modelMapper.map(productBluePrintRepository.save(productBluePrint), EditProductBluePrintDto.class);
    }

    @Override
    public List<SizeColorInRawProductDto> getColorSizeMapByColorByProductId(String productId) {
        List<SizeColor> sizeColors = sizeColorRepository.findSizeColorByProductId(productId);
        Map<String,List<String>> map = new HashMap<>();
        for (SizeColor sizeColor:sizeColors) {
                map.put(sizeColor.getColorNameImage(),getListSizeByColorName(sizeColors,sizeColor.getColor().getName()));
        }
        List<SizeColorInRawProductDto> list = new ArrayList<>();
        for (Map.Entry<String,List<String>> entry : map.entrySet()){
            list.add(SizeColorInRawProductDto.builder().color(entry.getKey()).sizes(entry.getValue()).build());
        }
        return list;

    }
    private List<String> getListSizeByColorName(List<SizeColor> sizeColor, String colorName){
        List<String> sizes = new ArrayList<>();
        for (int i = 0; i < sizeColor.size(); i++) {
            if(sizeColor.get(i).getColor().getName().equals(colorName)){
                sizes.add(sizeColor.get(i).getSize().getName());
            }
        }
        return sizes;
    }
}
