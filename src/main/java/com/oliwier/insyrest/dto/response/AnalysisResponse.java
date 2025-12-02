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
public class AnalysisResponse {

    @JsonProperty("aId")
    private Long aId;

    @JsonProperty("sId")
    private String sId;

    @JsonProperty("sStamp")
    private LocalDateTime sStamp;

    @JsonProperty("boxposString")
    private String boxposString;

    @JsonProperty("pol")
    private BigDecimal pol;

    @JsonProperty("nat")
    private BigDecimal nat;

    @JsonProperty("kal")
    private BigDecimal kal;

    @JsonProperty("an")
    private BigDecimal an;

    @JsonProperty("glu")
    private BigDecimal glu;

    @JsonProperty("dry")
    private BigDecimal dry;

    @JsonProperty("dateIn")
    private LocalDateTime dateIn;

    @JsonProperty("dateOut")
    private LocalDateTime dateOut;

    @JsonProperty("weightMea")
    private BigDecimal weightMea;

    @JsonProperty("weightNrm")
    private BigDecimal weightNrm;

    @JsonProperty("weightCur")
    private BigDecimal weightCur;

    @JsonProperty("weightDif")
    private BigDecimal weightDif;

    @JsonProperty("density")
    private BigDecimal density;

    @JsonProperty("aFlags")
    private String aFlags;

    @JsonProperty("lane")
    private Integer lane;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("dateExported")
    private LocalDateTime dateExported;
}
