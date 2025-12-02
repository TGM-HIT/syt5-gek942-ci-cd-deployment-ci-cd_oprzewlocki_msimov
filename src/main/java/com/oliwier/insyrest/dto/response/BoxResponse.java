package com.oliwier.insyrest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxResponse {

    @JsonProperty("bId")
    private String bId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("numMax")
    private Integer numMax;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("dateExported")
    private LocalDateTime dateExported;
}
