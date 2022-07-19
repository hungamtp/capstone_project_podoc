package com.capstone.pod.dto.rating;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRatingDTO {
    private float ratingStar;
    private String comment;
    private String designId;
}
