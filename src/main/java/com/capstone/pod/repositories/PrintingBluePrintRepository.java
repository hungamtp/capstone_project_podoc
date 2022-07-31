package com.capstone.pod.repositories;

import com.capstone.pod.entities.BluePrint;
import com.capstone.pod.entities.PrintingBluePrint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintingBluePrintRepository extends JpaRepository<PrintingBluePrint,String> {
}
