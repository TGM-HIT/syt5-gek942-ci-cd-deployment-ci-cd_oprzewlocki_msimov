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

    @Column(name = "s_id", length = 13)
    private String sId;

    @Column(name = "s_stamp")
    private LocalDateTime sStamp;

    @Column(name = "a_id")
    private Long aId;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    public Log() {}

    public Log(Long logId, LocalDateTime dateCreated, String level, String info,
               String sId, LocalDateTime sStamp, Long aId, LocalDateTime dateExported) {
        this.logId = logId;
        this.dateCreated = dateCreated;
        this.level = level;
        this.info = info;
        this.sId = sId;
        this.sStamp = sStamp;
        this.aId = aId;
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

    public String getSId() { return sId; }
    public void setSId(String sId) { this.sId = sId; }

    public LocalDateTime getSStamp() { return sStamp; }
    public void setSStamp(LocalDateTime sStamp) { this.sStamp = sStamp; }

    public Long getAId() { return aId; }
    public void setAId(Long aId) { this.aId = aId; }

    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }
}

