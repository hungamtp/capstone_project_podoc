package com.capstone.pod.services;


import com.capstone.pod.dto.follower.FollowerDTO;
import com.capstone.pod.entities.Follower;

import java.util.List;

public interface FollowerService {
    List<FollowerDTO> get();
    List<Follower> getUser();
    void follow(String idolId);
}
