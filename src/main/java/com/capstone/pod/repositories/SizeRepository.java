package com.capstone.pod.repositories;

import com.capstone.pod.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size,Integer> {
    Optional<Size> findByName(String name);
}
