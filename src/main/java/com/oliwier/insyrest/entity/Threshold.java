package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "threshold", schema = "venlab")
public class Threshold {

    @Id
    @Column(name = "th_id", length = 10)
    private String thId;

    @Column(name = "value_min", precision = 8, scale = 2)
    private BigDecimal valueMin;

    @Column(name = "value_max", precision = 8, scale = 2)
    private BigDecimal valueMax;

    @Column(name = "date_changed")
    private LocalDateTime dateChanged;
}
