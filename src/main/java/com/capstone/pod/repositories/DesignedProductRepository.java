package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignedProductRepository extends JpaRepository<DesignedProduct,Integer> , DesignedProductRepositoryCustom {
}
