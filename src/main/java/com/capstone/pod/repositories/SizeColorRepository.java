package com.capstone.pod.repositories;

import com.capstone.pod.entities.Color;
import com.capstone.pod.entities.Product;
import com.capstone.pod.entities.Size;
import com.capstone.pod.entities.SizeColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeColorRepository extends JpaRepository<SizeColor,String> , SizeColorRepositoryCustom {
    public Page<SizeColor> findSizeColorByProductId(Pageable pageable, String productId);
    Optional<SizeColor> findSizeColorByColorAndSizeAndProduct(Color color , Size size , Product product);

    Optional<SizeColor> findByColorNameAndSizeNameAndProductId(String colorName, String sizeName, String productId);
    Optional<SizeColor> findByColorImageColorAndSizeNameAndProductId(String imageColor, String sizeName, String productId);
}
