package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignedProductRepository extends JpaRepository<DesignedProduct,String> , DesignedProductRepositoryCustom, JpaSpecificationExecutor<DesignedProduct> {
    Page<DesignedProduct> findAllByUserId(Pageable page, String userId);
    long countAllByUser(User user);
}
