package com.capstone.pod.repositories;

import com.capstone.pod.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findUserByUsername(String username);
    public Optional<User> findUserByEmail(String email);
}
