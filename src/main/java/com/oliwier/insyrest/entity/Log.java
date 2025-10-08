package com.oliwier.insyrest.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "log", schema = "venlab")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "level", length = 10)
    private String level;

    @Column(name = "info", length = 255)
    private String info;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "s_id", referencedColumnName = "s_id", insertable = false, updatable = false),
            @JoinColumn(name = "s_stamp", referencedColumnName = "s_stamp", insertable = false, updatable = false)
    })
    private Sample sample;


    @ManyToOne
    @JoinColumn(name = "a_id", referencedColumnName = "a_id", insertable = false, updatable = false)
    private Analysis analysis;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    public Log() {}

    public Log(Long logId, LocalDateTime dateCreated, String level, String info, Sample sample, Analysis analysis,
               LocalDateTime dateExported) {
        this.logId = logId;
        this.dateCreated = dateCreated;
        this.level = level;
        this.info = info;
        this.sample = sample;
        this.analysis = analysis;

        this.dateExported = dateExported;
    }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }


    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }
}

