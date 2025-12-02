package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.SampleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Table(name = "sample_boxpos", schema = "venlab")
@Immutable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SampleId.class)
public class SampleBoxPosView {

    @Id
    @Column(name = "s_id")
    private String sId;

    @Id
    @Column(name = "s_stamp")
    private LocalDateTime sStamp;

    @Column(name = "boxpos")
    private String boxpos;
}
