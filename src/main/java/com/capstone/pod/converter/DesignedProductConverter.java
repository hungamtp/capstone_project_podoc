package com.capstone.pod.converter;

import com.capstone.pod.dto.designedProduct.DesignedProductDto;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.OrderDetail;
import com.capstone.pod.entities.Rating;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DesignedProductConverter {

    public DesignedProductDto entityToDesignedProductDto(DesignedProduct designedProduct) {
        List<OrderDetail> orderIsPaid = designedProduct.getOrderDetails()
            .stream()
            .filter(orderDetail -> orderDetail.getOrders().isPaid())
            .collect(Collectors.toList());
        return DesignedProductDto.builder()
            .id(designedProduct.getId())
            .name(designedProduct.getName())
            .rate(designedProduct.getRatings() == null ? 0 : designedProduct.getRatings().stream().mapToDouble(Rating::getRatingStar).average().orElse(0))
            .designedPrice(designedProduct.getDesignedPrice() + designedProduct.getPriceByFactory().getPrice())
            .image(designedProduct.getImagePreviews().stream().filter(imagePreview -> imagePreview.getPosition().equals("front")).collect(Collectors.toList()).get(0).getImage())
            .userId(designedProduct.getUser().getId())
            .username(designedProduct.getUser().getFirstName() + " " + designedProduct.getUser().getLastName())
            .soldCount(Integer.valueOf(orderIsPaid.stream().mapToInt(OrderDetail::getQuantity).sum()).longValue())
            .build();
    }
}
