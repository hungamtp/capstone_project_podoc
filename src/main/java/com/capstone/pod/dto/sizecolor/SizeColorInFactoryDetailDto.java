package com.capstone.pod.dto.sizecolor;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SizeColorInFactoryDetailDto implements Comparable<SizeColorInFactoryDetailDto> {
    int quantity;
    String size;
    String colorImage;
    @Override
    public int compareTo(@NotNull SizeColorInFactoryDetailDto o) {
        return this.colorImage.equals(o.getColorImage()) ? 0: 1;
    }
}
