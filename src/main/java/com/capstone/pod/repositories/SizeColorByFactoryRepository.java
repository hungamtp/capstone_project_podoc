package com.capstone.pod.repositories;

import com.capstone.pod.entities.Factory;
import com.capstone.pod.entities.SizeColor;
import com.capstone.pod.entities.SizeColorByFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeColorByFactoryRepository extends JpaRepository<SizeColorByFactory , Integer> {
    Optional<SizeColorByFactory> findByFactoryAndSizeColor(Factory factory , SizeColor sizeColor);
}
