package com.capstone.pod.repositories;

import com.capstone.pod.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag , Integer> {
}
