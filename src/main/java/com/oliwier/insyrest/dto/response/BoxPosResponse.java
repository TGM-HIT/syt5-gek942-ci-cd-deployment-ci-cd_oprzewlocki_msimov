package com.oliwier.insyrest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxPosResponse {

    private Integer bposId;
    private String bId;
    private String sId;
    private LocalDateTime sStamp;
    private LocalDateTime dateExported;
}
