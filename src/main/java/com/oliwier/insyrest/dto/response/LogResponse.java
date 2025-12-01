package com.oliwier.insyrest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {

    private Long logId;
    private LocalDateTime dateCreated;
    private String level;
    private String info;
    private String sId;
    private LocalDateTime sStamp;
    private Long aId;
    private LocalDateTime dateExported;
}
