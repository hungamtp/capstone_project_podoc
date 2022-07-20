package com.capstone.pod.converter;

import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.OrderDetailDto;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class OrderDetailConverter {

    public MyOrderDetailDto entityToMyOrderDetailDto (OrderDetail orderDetail){
        DesignedProduct designedProduct = orderDetail.getDesignedProduct();
        return MyOrderDetailDto.builder()
            .id(orderDetail.getId())
            .price(designedProduct.getPriceByFactory().getPrice() + designedProduct.getDesignedPrice())
            .designId(designedProduct.getId())
            .designName(designedProduct.getName())
            .designImage(designedProduct.getImagePreviews().stream().collect(Collectors.toList()).get(0).getImage())
            .designerName(designedProduct.getUser().getFirstName() + " " + designedProduct.getUser().getLastName())
            .designerId(designedProduct.getUser().getId())
            .color(orderDetail.getColor())
            .size(orderDetail.getSize())
            .quantity(orderDetail.getQuantity())
            .date(orderDetail.getOrders().getCreateDate())
            .build();
    }
}
