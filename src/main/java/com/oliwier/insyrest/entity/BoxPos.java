package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.BoxPosId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
