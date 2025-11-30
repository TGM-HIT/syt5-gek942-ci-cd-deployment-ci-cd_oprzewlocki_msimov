package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "box", schema = "venlab")
public class Box {

    @Id
    @Column(name = "b_id", length = 4)
    private String bId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "num_max")
    private Integer numMax;

    @Column(name = "type")
    private Integer type;

    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;
}
