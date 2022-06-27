package com.capstone.pod.repositories;

import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken,Integer> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByCredential(Credential credential);

}
