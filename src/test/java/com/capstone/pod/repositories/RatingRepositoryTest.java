package com.capstone.pod.repositories;

import com.capstone.pod.entities.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RatingRepositoryTest {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private DesignedProductRepository designedProductRepository;

//    @Test
//    public void test(){
//       Page<Rating> page= ratingRepository.findAllByDesignedProduct(designedProductRepository.findById("288a6a96-035c-4a2e-95a6-bc217810f67c").get() , PageRequest.of(1 , 1));
//
//    }

}
