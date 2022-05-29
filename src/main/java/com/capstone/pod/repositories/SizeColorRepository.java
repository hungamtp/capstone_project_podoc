package com.capstone.pod.repositories;

import com.capstone.pod.entities.SizeColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeColorRepository extends JpaRepository<SizeColor,Integer> {
    public Page<SizeColor> findSizeColorByProductId(Pageable pageable, String productId);
}
