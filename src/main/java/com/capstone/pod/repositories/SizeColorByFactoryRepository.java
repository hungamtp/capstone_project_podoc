package com.capstone.pod.repositories;

import com.capstone.pod.entities.Factory;
import com.capstone.pod.entities.SizeColor;
import com.capstone.pod.entities.SizeColorByFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizeColorByFactoryRepository extends JpaRepository<SizeColorByFactory , String> {
    Optional<SizeColorByFactory> findByFactoryAndSizeColor(Factory factory , SizeColor sizeColor);
    List<SizeColorByFactory> findAllBySizeColorProductId(String productId);

    Optional<SizeColorByFactory> findByFactoryIdAndSizeColorId(String factoryId, String sizeColorId);
}
