package com.oliwier.insyrest.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}

