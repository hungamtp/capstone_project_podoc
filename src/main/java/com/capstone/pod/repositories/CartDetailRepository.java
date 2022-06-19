package com.capstone.pod.repositories;

import com.capstone.pod.entities.CartDetail;
import com.capstone.pod.entities.DesignedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail , Integer> {
    Optional<CartDetail> findByDesignedProductAndColorAndSize(DesignedProduct designedProduct , String color , String size);
}
