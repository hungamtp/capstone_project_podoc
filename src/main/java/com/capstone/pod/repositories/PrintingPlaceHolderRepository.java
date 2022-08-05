package com.capstone.pod.repositories;

import com.capstone.pod.entities.PrintingPlaceholder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintingPlaceHolderRepository extends JpaRepository<PrintingPlaceholder,String> {
}
