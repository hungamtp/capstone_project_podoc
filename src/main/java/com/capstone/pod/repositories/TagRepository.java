package com.capstone.pod.repositories;

import com.capstone.pod.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag , String> {
    Optional<Tag> findByName(String name);
}
