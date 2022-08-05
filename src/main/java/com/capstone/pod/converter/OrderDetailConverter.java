package com.capstone.pod.converter;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.OrderDetailDto;
import com.capstone.pod.entities.Color;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.OrderDetail;
import com.capstone.pod.repositories.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDetailConverter {

    private final ColorRepository colorRepository;

    public MyOrderDetailDto entityToMyOrderDetailDto (OrderDetail orderDetail){
        DesignedProduct designedProduct = orderDetail.getDesignedProduct();
        Color color = colorRepository.findByName(orderDetail.getColor()).orElseThrow(
            () -> new EntityNotFoundException(EntityName.COLOR + "_" + ErrorMessage.NOT_FOUND)
        );
        return MyOrderDetailDto.builder()
            .id(orderDetail.getId())
            .price(designedProduct.getPriceByFactory().getPrice() + designedProduct.getDesignedPrice())
            .designId(designedProduct.getId())
            .designName(designedProduct.getName())
            .designImage(designedProduct.getImagePreviews()
                .stream()
                .filter(imagePreview -> imagePreview.getPosition().equalsIgnoreCase("front"))
                .filter(imagePreview -> imagePreview.getColor().equalsIgnoreCase(color.getImageColor()))
                .collect(Collectors.toList()).get(0).getImage())
            .designerName(designedProduct.getUser().getFirstName() + " " + designedProduct.getUser().getLastName())
            .designerId(designedProduct.getUser().getId())
            .color(orderDetail.getColor())
            .size(orderDetail.getSize())
            .quantity(orderDetail.getQuantity())
            .provider(orderDetail.getFactory().getName())
            .date(orderDetail.getOrders().getCreateDate())
            .isRated(orderDetail.isRate())
            .status(orderDetail.latestStatus())
            .build();
    }
}
