package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RatingRepository extends JpaRepository<Rating , String> {
    List<Rating> findAllByDesignedProductId(String designId);
    Page<Rating> findAllByDesignedProduct(DesignedProduct designedProduct , Pageable pageable);
}
