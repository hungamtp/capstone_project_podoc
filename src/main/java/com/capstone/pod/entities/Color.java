package com.capstone.pod.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Color {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String imageColor;
    @OneToMany(mappedBy = "color")
    private Collection<SizeColor> sizeColors;



}
