package com.capstone.pod.repositories;

import com.capstone.pod.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material,String> {
    Optional<Material> findByName(String name);
}
