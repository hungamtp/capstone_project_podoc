package com.capstone.pod.repositories;

import com.capstone.pod.entities.Follower;
import com.capstone.pod.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface FollowerRepository extends JpaRepository<Follower , String> {
    List<Follower> findByFollower(User user);
    List<Follower> findByUser(User user);
    Optional<Follower> findByFollowerAndUser(User follower , User idol);
}
