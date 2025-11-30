package com.oliwier.insyrest.entity.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;


@Embeddable
public class SampleId implements Serializable {

    @JsonProperty("s_id")
    @Column(name = "s_id", length = 13)
    private String sId;

    @JsonProperty("s_stamp")
    @Column(name = "s_stamp", nullable = false)
    private LocalDateTime sStamp;

    public SampleId(String sId, LocalDateTime sStamp) {
        this.sId = sId;
        this.sStamp = sStamp.withNano(0);
    }

    public SampleId() {
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public LocalDateTime getsStamp() {
        return sStamp;
    }

    public void setsStamp(LocalDateTime sStamp) {
        this.sStamp = sStamp;
    }
}