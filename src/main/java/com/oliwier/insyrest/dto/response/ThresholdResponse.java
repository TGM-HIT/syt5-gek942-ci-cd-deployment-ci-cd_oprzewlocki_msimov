package com.oliwier.insyrest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdResponse {

    private String thId;
    private BigDecimal valueMin;
    private BigDecimal valueMax;
    private LocalDateTime dateChanged;
}
