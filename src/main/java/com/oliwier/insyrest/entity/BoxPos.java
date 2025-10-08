package com.oliwier.insyrest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "boxpos", schema = "venlab")
public class BoxPos {

    @EmbeddedId
    private BoxPosId id;

    @Column(name = "s_id", length = 13)
    private String sId;

    @Column(name = "s_stamp")
    private LocalDateTime sStamp;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    public BoxPos() {}

    public BoxPos(BoxPosId id, String sId, LocalDateTime sStamp, LocalDateTime dateExported) {
        this.id = id;
        this.sId = sId;
        this.sStamp = sStamp;
        this.dateExported = dateExported;
    }

    public BoxPosId getId() { return id; }
    public void setId(BoxPosId id) { this.id = id; }

    public String getSId() { return sId; }
    public void setSId(String sId) { this.sId = sId; }

    public LocalDateTime getSStamp() { return sStamp; }
    public void setSStamp(LocalDateTime sStamp) { this.sStamp = sStamp; }

    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }
}
