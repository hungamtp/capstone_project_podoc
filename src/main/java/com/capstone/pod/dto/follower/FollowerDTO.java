package com.capstone.pod.dto.follower;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FollowerDTO {
    private String id;
    private String firstName;
    private String lastName;
}
