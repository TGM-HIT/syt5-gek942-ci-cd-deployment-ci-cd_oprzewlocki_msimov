package com.oliwier.insyrest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxResponse {

    private String bId;
    private String name;
    private Integer numMax;
    private Integer type;
    private String comment;
    private LocalDateTime dateExported;
}
