package com.capstone.pod.repositories;

import com.capstone.pod.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RatingRepository extends JpaRepository<Rating , Integer> {
    List<Rating> findAllByDesignedProductId(int designId);
}
