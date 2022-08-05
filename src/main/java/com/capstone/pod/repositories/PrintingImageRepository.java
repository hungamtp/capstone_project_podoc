package com.capstone.pod.repositories;

import com.capstone.pod.entities.PrintingImagePreview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintingImageRepository extends JpaRepository<PrintingImagePreview,String> {
}
