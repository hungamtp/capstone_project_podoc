package com.capstone.pod.repositories;

import com.capstone.pod.entities.Product;
import com.capstone.pod.entities.SizeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizeProductRepository extends JpaRepository<SizeProduct, String> {
    List<SizeProduct> findALlByProduct(Product product);
    Optional<SizeProduct> findByProductAndSize(Product product ,String size );
}
