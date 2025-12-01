package com.oliwier.insyrest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    private String sId;
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
