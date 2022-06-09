package com.capstone.pod.dto.product;

import com.capstone.pod.dto.tag.TagDto;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagDto {
    private TagDto tag;
}
