package com.capstone.pod.repositories;

import com.capstone.pod.entities.ProductBluePrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBluePrintRepository extends JpaRepository<ProductBluePrint,String> {
   public List<ProductBluePrint> getAllByProductId(String productId);
}
