package com.capstone.pod.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrintingInfo {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @OneToMany(mappedBy = "printingInfo",cascade = CascadeType.ALL)
    private List<PrintingImagePreview> previewImages;
    @OneToMany(mappedBy = "printingInfo",cascade = CascadeType.ALL)
    List<PrintingBluePrint> printingBluePrints;
    @OneToOne
    private OrderDetail orderDetail;
}
