package com.oliwier.insyrest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxPosResponse {

    @JsonProperty("bposId")
    private Integer bposId;

    @JsonProperty("bId")
    private String bId;

    @JsonProperty("sId")
    private String sId;

    @JsonProperty("sStamp")
    private LocalDateTime sStamp;

    @JsonProperty("dateExported")
    private LocalDateTime dateExported;
}
