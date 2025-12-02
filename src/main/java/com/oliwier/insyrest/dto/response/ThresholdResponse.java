package com.oliwier.insyrest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdResponse {

    @JsonProperty("thId")
    private String thId;

    @JsonProperty("valueMin")
    private BigDecimal valueMin;

    @JsonProperty("valueMax")
    private BigDecimal valueMax;

    @JsonProperty("dateChanged")
    private LocalDateTime dateChanged;
}
