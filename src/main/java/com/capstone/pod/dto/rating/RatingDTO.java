package com.capstone.pod.dto.rating;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    private String comment;
    private float ratingStar;
    private String user;
    private String userAvatar;
    private LocalDate date;
}
