package com.capstone.pod.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BluePrint {
    @Id
    @GeneratedValue
    private int id;
    private String frameImage;
    private String position;

    @OneToOne
    @JoinColumn(name = "placeholderId",referencedColumnName = "id")
    Placeholder placeholder;
}
