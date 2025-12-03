package com.oliwier.insyrest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private Integer numMax;
    private Integer type;

    @Size(max = 255, message = "Comment must not exceed 255 characters")
    private String comment;
    private LocalDateTime dateExported;
}
