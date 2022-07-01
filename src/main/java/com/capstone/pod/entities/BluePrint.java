package com.capstone.pod.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String frameImage;
    private String position;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    Placeholder placeholder;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bluePrint")
    List<DesignInfo> designInfos;
    @ManyToOne
    DesignedProduct designedProduct;
}
