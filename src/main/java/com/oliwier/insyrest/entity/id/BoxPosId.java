package com.oliwier.insyrest.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BoxPosId implements Serializable {

    @Column(name = "bpos_id")
    private Integer bposId;

    @Column(name = "b_id", length = 4)
    private String bId;

    public BoxPosId() {}

    public BoxPosId(Integer bposId, String bId) {
        this.bposId = bposId;
        this.bId = bId;
    }

    public Integer getBposId() { return bposId; }
    public void setBposId(Integer bposId) { this.bposId = bposId; }

    public String getBId() { return bId; }
    public void setBId(String bId) { this.bId = bId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoxPosId that)) return false;
        return Objects.equals(bposId, that.bposId) &&
                Objects.equals(bId, that.bId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bposId, bId);
    }
}