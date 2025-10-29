package com.oliwier.insyrest.dto;

public record BoxPosDto(
        Integer bposId,
        String bId,
        String sId,
        String sStamp,
        String dateExported
) {}

