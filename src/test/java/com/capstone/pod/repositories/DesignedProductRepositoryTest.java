package com.capstone.pod.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DesignedProductRepositoryTest {

    @Autowired
    private DesignedProductRepository designedProductRepository;

    @Test
    public void testGetBestSellerDesignedProduct(){
        designedProductRepository.get4HighestRateDesignedProduct();
    }

}
