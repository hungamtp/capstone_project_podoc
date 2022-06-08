package com.capstone.pod.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BluePrint {
    @Id
    @GeneratedValue
    private int id;
    private String frameImage;
    private String position;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    Placeholder placeholder;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    List<DesignInfo> designInfos;
    @ManyToOne
    DesignedProduct designedProduct;
}
