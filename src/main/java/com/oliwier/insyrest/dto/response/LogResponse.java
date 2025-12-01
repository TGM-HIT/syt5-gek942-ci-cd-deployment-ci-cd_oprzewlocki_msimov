package com.oliwier.insyrest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {

    @JsonProperty("logId")
    private Long logId;

    @JsonProperty("dateCreated")
    private LocalDateTime dateCreated;

    @JsonProperty("level")
    private String level;

    @JsonProperty("info")
    private String info;

    @JsonProperty("sId")
    private String sId;

    @JsonProperty("sStamp")
    private LocalDateTime sStamp;

    @JsonProperty("aId")
    private Long aId;

    @JsonProperty("dateExported")
    private LocalDateTime dateExported;
}
