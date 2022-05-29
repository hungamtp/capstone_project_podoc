package com.capstone.pod.repositories;

import com.capstone.pod.entities.Category;
import com.capstone.pod.entities.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImagesRepository extends JpaRepository<ProductImages,Integer> {
}
