package com.oliwier.insyrest.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "sample", schema = "venlab")
public class Sample {

    public Sample(SampleId id, String name, BigDecimal weightNet, BigDecimal weightBru, BigDecimal weightTar, Integer quantity, BigDecimal distance, LocalDateTime dateCrumbled, String sFlags, Integer lane, String comment, LocalDateTime dateExported) {
        this.id = id;
        this.name = name;
        this.weightNet = weightNet;
        this.weightBru = weightBru;
        this.weightTar = weightTar;
        this.quantity = quantity;
        this.distance = distance;
        this.dateCrumbled = dateCrumbled;
        this.sFlags = sFlags;
        this.lane = lane;
        this.comment = comment;
        this.dateExported = dateExported;
    }

    public Sample() {
    }

    public SampleId getId() {
        return id;
    }

    public void setId(SampleId id) {
        this.id = id;
    }

    @EmbeddedId
    @JsonUnwrapped   // Damit JSON flach ausgegeben wird
    private SampleId id;

    @Column(name = "name")
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

    private String comment;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWeightNet() {
        return weightNet;
    }

    public void setWeightNet(BigDecimal weightNet) {
        this.weightNet = weightNet;
    }

    public BigDecimal getWeightBru() {
        return weightBru;
    }

    public void setWeightBru(BigDecimal weightBru) {
        this.weightBru = weightBru;
    }

    public BigDecimal getWeightTar() {
        return weightTar;
    }

    public void setWeightTar(BigDecimal weightTar) {
        this.weightTar = weightTar;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public LocalDateTime getDateCrumbled() {
        return dateCrumbled;
    }

    public void setDateCrumbled(LocalDateTime dateCrumbled) {
        this.dateCrumbled = dateCrumbled;
    }

    public String getsFlags() {
        return sFlags;
    }

    public void setsFlags(String sFlags) {
        this.sFlags = sFlags;
    }

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateExported() {
        return dateExported;
    }

    public void setDateExported(LocalDateTime dateExported) {
        this.dateExported = dateExported;
    }
}

