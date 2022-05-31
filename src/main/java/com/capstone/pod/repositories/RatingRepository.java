package com.capstone.pod.repositories;

import com.capstone.pod.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating , Integer> {
}
