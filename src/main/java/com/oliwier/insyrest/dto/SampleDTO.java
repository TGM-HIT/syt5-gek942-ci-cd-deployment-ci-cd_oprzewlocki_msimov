package com.oliwier.insyrest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SampleDTO {

    public String s_id;
    public LocalDateTime s_stamp;

    public String name;
    public BigDecimal weightNet;
    public BigDecimal weightBru;
    public BigDecimal weightTar;
    public Integer quantity;
    public BigDecimal distance;
    public LocalDateTime dateCrumbled;
    public String sFlags;
    public Integer lane;
    public String comment;
    public LocalDateTime dateExported;

    public String boxposString;

}
