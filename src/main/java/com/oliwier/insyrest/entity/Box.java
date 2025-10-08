package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    // --- Constructors ---
    public Box() {}

    public Box(String bId, String name, Integer numMax, Integer type, String comment, LocalDateTime dateExported) {
        this.bId = bId;
        this.name = name;
        this.numMax = numMax;
        this.type = type;
        this.comment = comment;
        this.dateExported = dateExported;
    }

    public String getBId() { return bId; }
    public void setBId(String bId) { this.bId = bId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getNumMax() { return numMax; }
    public void setNumMax(Integer numMax) { this.numMax = numMax; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }
}
