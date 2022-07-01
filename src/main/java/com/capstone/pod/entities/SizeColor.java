package com.capstone.pod.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

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
    private Collection<SizeColorByFactory> sizeColorByFactories;

}
