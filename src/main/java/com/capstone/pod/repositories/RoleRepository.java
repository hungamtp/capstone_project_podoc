package com.capstone.pod.repositories;

import com.capstone.pod.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
  public Role findByName(String name);
}
