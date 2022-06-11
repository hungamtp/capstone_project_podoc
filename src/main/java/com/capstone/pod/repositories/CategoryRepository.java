package com.capstone.pod.repositories;

import com.capstone.pod.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    public Optional<Category> findByName(String name);
    List<Category> findAllByIsDeletedFalse();
}
