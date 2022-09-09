package com.capstone.pod.controller;

import com.capstone.pod.services.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("follower")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @GetMapping
    public ResponseEntity get() {
        return ResponseEntity.ok().body(followerService.get());
    }

    @PostMapping("/{idolId}")
    public ResponseEntity follow(@PathVariable String idolId) {
        followerService.follow(idolId);
        return ResponseEntity.ok().body(true);
    }
}
