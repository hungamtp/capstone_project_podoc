package com.capstone.pod.services.implement;

import com.capstone.pod.entities.Follower;
import com.capstone.pod.entities.User;
import com.capstone.pod.repositories.FollowerRepository;
import com.capstone.pod.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowerServiceImplTest {
    @Autowired
    private FollowerRepository followerRepository ;
    @Autowired
    private UserRepository userRepository;

    private static final String  u1 = "1880aacf-9a00-475a-9c34-92881a5acee0";
    private static final String  u2 = "21ebc2b9-b10e-40f5-9b2b-710ef95acbd2";

    @Test
    public void tesst(){
        User user1 = userRepository.findById(u1).get();
        User user2 = userRepository.findById(u2).get();
        followerRepository.save(Follower.builder()
                .user(user1)
                .follower(user2)
            .build());

        followerRepository.save(Follower.builder()
            .user(user2)
            .follower(user1)
            .build());

        List<Follower> followers = followerRepository.findByFollower(user1);
        List<Follower> user = followerRepository.findByFollower(user2);

    }

}
