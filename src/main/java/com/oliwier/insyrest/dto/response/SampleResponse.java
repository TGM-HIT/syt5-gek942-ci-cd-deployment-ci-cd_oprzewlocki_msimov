package com.oliwier.insyrest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleResponse {

    private String s_id;
    private LocalDateTime s_stamp;
    private String name;
    private BigDecimal weightNet;
    private BigDecimal weightBru;
    private BigDecimal weightTar;
    private Integer quantity;
    private BigDecimal distance;
    private LocalDateTime dateCrumbled;
    private String sFlags;
    private Integer lane;
    private String comment;
    private LocalDateTime dateExported;

    // Computed field
    private String boxposString;
}
