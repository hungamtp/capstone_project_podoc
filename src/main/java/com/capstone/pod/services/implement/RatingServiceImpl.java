package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.rating.AddRatingDTO;
import com.capstone.pod.dto.rating.RatingDTO;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.DesignedProductRepository;
import com.capstone.pod.repositories.OrderDetailRepository;
import com.capstone.pod.repositories.RatingRepository;
import com.capstone.pod.services.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;
    private DesignedProductRepository designedProductRepository;
    private CredentialRepository credentialRepository;
    private OrderDetailRepository orderDetailRepository;

    @Override
    public PageDTO getAllRatingByDesignedProduct(String designedProductId, Pageable pageable) {
        DesignedProduct designedProduct = designedProductRepository.findById(designedProductId).orElseThrow(
            () -> new IllegalArgumentException(EntityName.DESIGNED_PRODUCT + "_" + ErrorMessage.NOT_FOUND)
        );

        Page<Rating> ratingPage = ratingRepository.findAllByDesignedProduct(designedProduct, pageable);

        List<RatingDTO> result = ratingPage.stream().map(rating ->
        {
            User user = rating.getUser();
            return RatingDTO.builder()
                .ratingStar(rating.getRatingStar())
                .comment(rating.getComment())
                .date(rating.getRatingDate())
                .userAvatar(user.getCredential().getImage())
                .user(user.getFirstName() + " " + user.getLastName())
                .build();
        }).collect(Collectors.toList());
        return PageDTO.builder()
            .data(result)
            .page(ratingPage.getNumber())
            .elements(Long.valueOf(ratingPage.getTotalElements()).intValue())
            .build();
    }

    @Override
    public Boolean addComment(AddRatingDTO addRatingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId)
            .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));

        OrderDetail orderDetail = orderDetailRepository.findById(addRatingDTO.getOrderDetailId()).orElseThrow(
            () -> new IllegalArgumentException(EntityName.ORDERS_DETAIL + "_" + ErrorMessage.NOT_FOUND)
        );

        if (orderDetail.isRate()) {
            throw new IllegalArgumentException(EntityName.ORDERS_DETAIL + "_" + ErrorMessage.IS_RATED);
        }

        DesignedProduct designedProduct = designedProductRepository.findById(addRatingDTO.getDesignId()).orElseThrow(
            () -> new IllegalArgumentException(EntityName.DESIGNED_PRODUCT + "_" + ErrorMessage.NOT_FOUND)
        );
        Rating rating = Rating.builder()
            .comment(addRatingDTO.getComment())
            .ratingStar(addRatingDTO.getRatingStar())
            .ratingDate(LocalDate.now())
            .user(credential.getUser())
            .designedProduct(designedProduct)
            .build();

        ratingRepository.save(rating);
        orderDetail.setRate(true);
        orderDetailRepository.save(orderDetail);
        return true;
    }


}
