package com.oliwier.insyrest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AnalysisDTO {

    public Long aId;

    public String sId;
    public LocalDateTime sStamp;

    public String boxposString;

    public Integer lane;
    public String comment;

    public BigDecimal pol;
    public BigDecimal nat;
    public BigDecimal kal;
    public BigDecimal an;
    public BigDecimal glu;
    public BigDecimal dry;

    public LocalDateTime dateIn;
    public LocalDateTime dateOut;
}

