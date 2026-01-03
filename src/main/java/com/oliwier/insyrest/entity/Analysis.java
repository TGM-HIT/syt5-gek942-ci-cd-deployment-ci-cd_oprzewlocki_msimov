package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "analysis", schema = "venlab")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long aId;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumns({
            @JoinColumn(name = "s_id", referencedColumnName = "s_id"),
            @JoinColumn(name = "s_stamp", referencedColumnName = "s_stamp")
    })
    private Sample sample;

    @Column(name = "pol", precision = 8, scale = 2)
    private BigDecimal pol;

    @Column(name = "nat", precision = 8, scale = 2)
    private BigDecimal nat;

    @Column(name = "kal", precision = 8, scale = 2)
    private BigDecimal kal;

    @Column(name = "an", precision = 8, scale = 2)
    private BigDecimal an;

    @Column(name = "glu", precision = 8, scale = 2)
    private BigDecimal glu;

    @Column(name = "dry", precision = 8, scale = 2)
    private BigDecimal dry;

    @Column(name = "date_in")
    private LocalDateTime dateIn;

    @Column(name = "date_out")
    private LocalDateTime dateOut;

    @Column(name = "weight_mea", precision = 8, scale = 2)
    private BigDecimal weightMea;

    @Column(name = "weight_nrm", precision = 8, scale = 2)
    private BigDecimal weightNrm;

    @Column(name = "weight_cur", precision = 8, scale = 2)
    private BigDecimal weightCur;

    @Column(name = "weight_dif", precision = 8, scale = 2)
    private BigDecimal weightDif;

    @Column(name = "density", precision = 8, scale = 2)
    private BigDecimal density;

    @Column(name = "a_flags", length = 15)
    private String aFlags;

    @Column(name = "lane")
    private Integer lane;

    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;
}
