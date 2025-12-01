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
public class AnalysisRequest {

    @NotNull(message = "Sample ID is required")
    private String sId;

    @NotNull(message = "Sample timestamp is required")
    private LocalDateTime sStamp;

    private BigDecimal pol;
    private BigDecimal nat;
    private BigDecimal kal;
    private BigDecimal an;
    private BigDecimal glu;
    private BigDecimal dry;
    private LocalDateTime dateIn;
    private LocalDateTime dateOut;
    private BigDecimal weightMea;
    private BigDecimal weightNrm;
    private BigDecimal weightCur;
    private BigDecimal weightDif;
    private BigDecimal density;
    private String aFlags;
    private Integer lane;
    private String comment;
    private LocalDateTime dateExported;
}
