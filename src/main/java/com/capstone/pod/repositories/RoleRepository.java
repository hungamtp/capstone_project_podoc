package com.capstone.pod.repositories;

import com.capstone.pod.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
  public Optional<Role> findByName(String name);
}
