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
public class PrintingBluePrint {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String frameImage;
    private String position;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "printingBluePrint")
    PrintingPlaceholder printingPlaceholder;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "printingBluePrint")
    List<PrintingDesignInfo> printingDesignInfos;
    @ManyToOne
    PrintingInfo printingInfo;
}
