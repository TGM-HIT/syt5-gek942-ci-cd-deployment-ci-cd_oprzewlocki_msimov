package com.oliwier.insyrest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxPosRequest {

    @NotNull(message = "Box position ID is required")
    private Integer bposId;

    @NotNull(message = "Box ID is required")
    private String bId;

    @NotNull(message = "Sample ID is required")
    private String sId;

    @NotNull(message = "Sample timestamp is required")
    private LocalDateTime sStamp;

    private LocalDateTime dateExported;
}
