package com.oliwier.insyrest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxRequest {

    @NotNull(message = "Box ID is required")
    private String bId;

    private String name;
    private Integer numMax;
    private Integer type;
    private String comment;
    private LocalDateTime dateExported;
}
