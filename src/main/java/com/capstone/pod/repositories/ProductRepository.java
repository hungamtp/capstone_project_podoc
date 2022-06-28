package com.capstone.pod.repositories;

import com.capstone.pod.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
    public Optional<Product> findByName(String name);
    public List<Product> findAllByPriceByFactoriesFactoryId(int factoryId);
}
