package com.capstone.pod.converter;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.OrderStateDto;
import com.capstone.pod.entities.Color;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.OrderDetail;
import com.capstone.pod.repositories.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDetailConverter {

    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;

    public MyOrderDetailDto entityToMyOrderDetailDto(OrderDetail orderDetail) {
        DesignedProduct designedProduct = orderDetail.getDesignedProduct();
        Color color = colorRepository.findByName(orderDetail.getColor()).orElseThrow(
            () -> new EntityNotFoundException(EntityName.COLOR + "_" + ErrorMessage.NOT_FOUND)
        );
        var imagePreviews = designedProduct.getImagePreviews()
            .stream()
            .filter(imagePreview -> imagePreview.getPosition().equalsIgnoreCase("front"))
            .filter(imagePreview -> imagePreview.getColor().equalsIgnoreCase(color.getImageColor()))
            .collect(Collectors.toList());
        return MyOrderDetailDto.builder()
            .id(orderDetail.getId())
            .price(designedProduct.getPriceByFactory().getPrice() + designedProduct.getDesignedPrice())
            .designId(designedProduct.getId())
            .designName(designedProduct.getName())
            .designImage(imagePreviews.size() != 0 ? imagePreviews.get(0).getImage() : "")
            .designerName(designedProduct.getUser().getFirstName() + " " + designedProduct.getUser().getLastName())
            .designerId(designedProduct.getUser().getId())
            .color(orderDetail.getColor())
            .size(orderDetail.getSize())
            .quantity(orderDetail.getQuantity())
            .provider(orderDetail.getFactory().getName())
            .date(orderDetail.getOrders().getCreateDate())
            .isRated(orderDetail.isRate())
            .status(orderDetail.getOrders().isCanceled() ? "IS_CANCEL" : orderDetail.latestStatus())
            .isCancel(orderDetail.isCanceled())
            .reason(orderDetail.getReason())
            .statuses(orderDetail.getOrderStatuses().stream().map(orderStatus -> modelMapper.map(orderStatus, OrderStateDto.class)).collect(Collectors.toList()))
            .build();
    }
}
