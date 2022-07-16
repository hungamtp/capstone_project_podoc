package com.capstone.pod.repositories;

import com.capstone.pod.entities.ImagePreview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagePreviewRepository extends JpaRepository<ImagePreview,String> {
}
