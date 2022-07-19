package com.capstone.pod.services;

import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.rating.AddRatingDTO;
import org.springframework.data.domain.Pageable;

public interface RatingService {
    PageDTO getAllRatingByDesignedProduct(String designedProductId, Pageable pageable);
    Boolean addComment(AddRatingDTO addRatingDTO);
}
