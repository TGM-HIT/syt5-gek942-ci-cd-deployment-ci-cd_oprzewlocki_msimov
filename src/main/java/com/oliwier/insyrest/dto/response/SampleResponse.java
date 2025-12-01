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
public class SampleResponse {

    @JsonProperty("s_id")
    private String s_id;

    @JsonProperty("s_stamp")
    private LocalDateTime s_stamp;

    @JsonProperty("name")
    private String name;

    @JsonProperty("weightNet")
    private BigDecimal weightNet;

    @JsonProperty("weightBru")
    private BigDecimal weightBru;

    @JsonProperty("weightTar")
    private BigDecimal weightTar;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("distance")
    private BigDecimal distance;

    @JsonProperty("dateCrumbled")
    private LocalDateTime dateCrumbled;

    @JsonProperty("sFlags")
    private String sFlags;

    @JsonProperty("lane")
    private Integer lane;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("dateExported")
    private LocalDateTime dateExported;

    @JsonProperty("boxposString")
    private String boxposString;
}
