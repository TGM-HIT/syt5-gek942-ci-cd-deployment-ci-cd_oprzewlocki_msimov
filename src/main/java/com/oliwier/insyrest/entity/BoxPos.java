package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "boxpos", schema = "venlab")
public class BoxPos {

    @EmbeddedId
    private BoxPosId id;

    @ManyToOne
    @MapsId("bId")
    @JoinColumn(name = "b_id", referencedColumnName = "b_id")
    private Box box;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "s_id", referencedColumnName = "s_id"),
            @JoinColumn(name = "s_stamp", referencedColumnName = "s_stamp")
    })
    private Sample sample;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    public BoxPos() {}

    public BoxPos(BoxPosId id, Box box, Sample sample, LocalDateTime dateExported) {
        this.id = id;
        this.box = box;
        this.sample = sample;
        this.dateExported = dateExported;
    }

    public BoxPosId getId() { return id; }
    public void setId(BoxPosId id) { this.id = id; }

    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }
}
