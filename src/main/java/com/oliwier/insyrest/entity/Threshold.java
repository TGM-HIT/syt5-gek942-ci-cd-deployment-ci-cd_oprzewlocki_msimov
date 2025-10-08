package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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

    public Threshold() {}

    public Threshold(String thId, BigDecimal valueMin, BigDecimal valueMax, LocalDateTime dateChanged) {
        this.thId = thId;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.dateChanged = dateChanged;
    }

    public String getThId() { return thId; }
    public void setThId(String thId) { this.thId = thId; }

    public BigDecimal getValueMin() { return valueMin; }
    public void setValueMin(BigDecimal valueMin) { this.valueMin = valueMin; }

    public BigDecimal getValueMax() { return valueMax; }
    public void setValueMax(BigDecimal valueMax) { this.valueMax = valueMax; }

    public LocalDateTime getDateChanged() { return dateChanged; }
    public void setDateChanged(LocalDateTime dateChanged) { this.dateChanged = dateChanged; }
}
