package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignColorRepository extends JpaRepository<DesignColor,String> {
}
