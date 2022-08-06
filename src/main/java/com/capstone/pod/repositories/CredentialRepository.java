package com.capstone.pod.repositories;

import com.capstone.pod.entities.Credential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential,String> {
        public Optional<Credential> findCredentialByEmail(String email);
        public Optional<Credential> findCredentialByEmailContains(String email);
        public Page<Credential> findAllByRoleName(Pageable pageable, String roleName);
        public Page<Credential> findAllByUserFirstNameContains(Pageable pageable, String firstName);
}
