package com.capstone.pod.repositories;

import com.capstone.pod.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential,Integer> {
        public Optional<Credential> findCredentialByEmail(String email);
}
