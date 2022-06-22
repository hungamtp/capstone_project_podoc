package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignedProductRepository extends JpaRepository<DesignedProduct,Integer> , DesignedProductRepositoryCustom, JpaSpecificationExecutor<DesignedProduct> {
    Page<DesignedProduct> findAllByUserId(Pageable page, int userId);
}
