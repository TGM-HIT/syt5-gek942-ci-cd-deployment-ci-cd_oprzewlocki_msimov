package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis", schema = "venlab")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long aId;

    @ManyToOne(cascade = CascadeType.PERSIST)
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

    public Analysis() {}

    public Analysis(Long aId, Sample sample, BigDecimal pol, BigDecimal nat, BigDecimal kal,
                    BigDecimal an, BigDecimal glu, BigDecimal dry, LocalDateTime dateIn, LocalDateTime dateOut,
                    BigDecimal weightMea, BigDecimal weightNrm, BigDecimal weightCur, BigDecimal weightDif,
                    BigDecimal density, String aFlags, Integer lane, String comment, LocalDateTime dateExported) {
        this.aId = aId;
        this.sample = sample;
        this.pol = pol;
        this.nat = nat;
        this.kal = kal;
        this.an = an;
        this.glu = glu;
        this.dry = dry;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.weightMea = weightMea;
        this.weightNrm = weightNrm;
        this.weightCur = weightCur;
        this.weightDif = weightDif;
        this.density = density;
        this.aFlags = aFlags;
        this.lane = lane;
        this.comment = comment;
        this.dateExported = dateExported;
    }

    public Long getAId() { return aId; }
    public void setAId(Long aId) { this.aId = aId; }


    public BigDecimal getPol() { return pol; }
    public void setPol(BigDecimal pol) { this.pol = pol; }

    public BigDecimal getNat() { return nat; }
    public void setNat(BigDecimal nat) { this.nat = nat; }

    public BigDecimal getKal() { return kal; }
    public void setKal(BigDecimal kal) { this.kal = kal; }

    public BigDecimal getAn() { return an; }
    public void setAn(BigDecimal an) { this.an = an; }

    public BigDecimal getGlu() { return glu; }
    public void setGlu(BigDecimal glu) { this.glu = glu; }

    public BigDecimal getDry() { return dry; }
    public void setDry(BigDecimal dry) { this.dry = dry; }

    public LocalDateTime getDateIn() { return dateIn; }
    public void setDateIn(LocalDateTime dateIn) { this.dateIn = dateIn; }

    public LocalDateTime getDateOut() { return dateOut; }
    public void setDateOut(LocalDateTime dateOut) { this.dateOut = dateOut; }

    public BigDecimal getWeightMea() { return weightMea; }
    public void setWeightMea(BigDecimal weightMea) { this.weightMea = weightMea; }

    public BigDecimal getWeightNrm() { return weightNrm; }
    public void setWeightNrm(BigDecimal weightNrm) { this.weightNrm = weightNrm; }

    public BigDecimal getWeightCur() { return weightCur; }
    public void setWeightCur(BigDecimal weightCur) { this.weightCur = weightCur; }

    public BigDecimal getWeightDif() { return weightDif; }
    public void setWeightDif(BigDecimal weightDif) { this.weightDif = weightDif; }

    public BigDecimal getDensity() { return density; }
    public void setDensity(BigDecimal density) { this.density = density; }

    public String getAFlags() { return aFlags; }
    public void setAFlags(String aFlags) { this.aFlags = aFlags; }

    public Integer getLane() { return lane; }
    public void setLane(Integer lane) { this.lane = lane; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

}
