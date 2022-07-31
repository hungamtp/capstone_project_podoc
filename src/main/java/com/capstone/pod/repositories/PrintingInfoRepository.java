package com.capstone.pod.repositories;

import com.capstone.pod.entities.BluePrint;
import com.capstone.pod.entities.PrintingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintingInfoRepository extends JpaRepository<PrintingInfo,String> {
}
