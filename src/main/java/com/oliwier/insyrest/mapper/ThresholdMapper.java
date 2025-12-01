package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.ThresholdRequest;
import com.oliwier.insyrest.dto.response.ThresholdResponse;
import com.oliwier.insyrest.entity.Threshold;
import org.springframework.stereotype.Component;

@Component
public class ThresholdMapper implements EntityMapper<Threshold, ThresholdRequest, ThresholdResponse> {

    @Override
    public Threshold toEntity(ThresholdRequest request) {
        Threshold threshold = new Threshold();
        threshold.setThId(request.getThId());
        threshold.setValueMin(request.getValueMin());
        threshold.setValueMax(request.getValueMax());
        threshold.setDateChanged(request.getDateChanged());
        return threshold;
    }

    @Override
    public ThresholdResponse toResponse(Threshold entity) {
        ThresholdResponse response = new ThresholdResponse();
        response.setThId(entity.getThId());
        response.setValueMin(entity.getValueMin());
        response.setValueMax(entity.getValueMax());
        response.setDateChanged(entity.getDateChanged());
        return response;
    }

    @Override
    public void updateEntity(Threshold entity, ThresholdRequest request) {
        // Don't update thId - it's the primary key
        entity.setValueMin(request.getValueMin());
        entity.setValueMax(request.getValueMax());
        entity.setDateChanged(request.getDateChanged());
    }
}
