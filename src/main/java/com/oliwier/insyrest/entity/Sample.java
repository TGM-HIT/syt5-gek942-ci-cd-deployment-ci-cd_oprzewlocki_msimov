package com.oliwier.insyrest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.oliwier.insyrest.entity.id.SampleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sample", schema = "venlab")
public class Sample {
    @EmbeddedId
    @JsonUnwrapped   // Damit JSON flach ausgegeben wird
    private SampleId id;

    @Column(name = "name", length = 500)
    private String name;

    @Column(name = "weight_net", precision = 8, scale = 2)
    private BigDecimal weightNet;

    @Column(name = "weight_bru", precision = 8, scale = 2)
    private BigDecimal weightBru;

    @Column(name = "weight_tar", precision = 8, scale = 2)
    private BigDecimal weightTar;

    private Integer quantity;

    @Column(precision = 8, scale = 2)
    private BigDecimal distance;

    @Column(name = "date_crumbled")
    private LocalDateTime dateCrumbled;

    @Column(name = "s_flags", length = 10)
    private String sFlags;

    private Integer lane;

    @Column(length = 1000)
    private String comment;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    @OneToMany(mappedBy = "sample")
    @JsonIgnore
    private Set<BoxPos> boxPositions = new HashSet<>();
}
