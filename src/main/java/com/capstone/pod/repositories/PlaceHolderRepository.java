package com.capstone.pod.repositories;

import com.capstone.pod.entities.Placeholder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceHolderRepository extends JpaRepository<Placeholder,String> {
}
