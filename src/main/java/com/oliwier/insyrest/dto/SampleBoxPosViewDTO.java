package com.oliwier.insyrest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleBoxPosViewDTO {
    private String sId;
    private LocalDateTime sStamp;
    private String boxpos;
}
