package com.oliwier.insyrest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdRequest {

    @NotNull(message = "Threshold ID is required")
    private String thId;

    private BigDecimal valueMin;
    private BigDecimal valueMax;
    private LocalDateTime dateChanged;
}
