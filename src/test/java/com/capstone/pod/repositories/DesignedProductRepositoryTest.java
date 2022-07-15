package com.capstone.pod.repositories;

import com.capstone.pod.dto.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class DesignedProductRepositoryTest {

    @Autowired
    private DesignedProductRepository designedProductRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    public void testGetBestSellerDesignedProduct() {
       Double a = ordersRepository.getInComeByUserId("1880aacf-9a00-475a-9c34-92881a5acee0" , LocalDate.now().minusYears(1) , LocalDate.now());
        System.out.println(a);
    }

}
