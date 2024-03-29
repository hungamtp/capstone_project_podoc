package com.capstone.pod.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SizeColor{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @ManyToOne
    private Color color;

    @ManyToOne
    private Size size;

    @ManyToOne
    private Product product;

    @OneToMany(mappedBy = "sizeColor")
    private List<SizeColorByFactory> sizeColorByFactories;
    public String getColorNameImage(){
        return color.getName()+"-"+color.getImageColor();
    }
}
