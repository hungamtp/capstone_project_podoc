package com.capstone.pod.repositories;

import com.capstone.pod.entities.Cart;
import com.capstone.pod.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart , Integer> {
     Cart findCartByUser(User user);
}
