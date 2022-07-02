package com.capstone.pod.repositories;

import com.capstone.pod.dto.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DesignedProductRepositoryTest {

    @Autowired
    private DesignedProductRepository designedProductRepository;

    @Test
    public void testGetBestSellerDesignedProduct() {
        String email = Utils.getEmailFromJwt("eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiZW1haWwiOiJodW5nbmVAZ21haWwuY29tIiwiY3JlZGVudGlhbElkIjoxNCwiaWF0IjoxNjU1MzAxNzk3LCJleHAiOjE2NTY0MzU2MDB9.SFeNkF7bLUFSXpZShjI4MBI3rDiWauSLL9NXxAmn6BDws5nMyrl1Ku76coQ5xBc29onMJGwZuQk7-XclWUaWoQ");
        System.out.println(email);
    }

}
