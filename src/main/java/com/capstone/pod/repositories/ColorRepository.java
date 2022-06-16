package com.capstone.pod.repositories;

import com.capstone.pod.entities.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color,Integer> {
    Optional<Color> findByName(String name);
    Optional<Color> findByImageColor(String imageColor);
}
