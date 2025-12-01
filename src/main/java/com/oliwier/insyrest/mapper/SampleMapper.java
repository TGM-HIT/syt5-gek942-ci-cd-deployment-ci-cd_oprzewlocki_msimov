package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.SampleRequest;
import com.oliwier.insyrest.dto.response.SampleResponse;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SampleMapper implements EntityMapper<Sample, SampleRequest, SampleResponse> {

    @Override
    public Sample toEntity(SampleRequest request) {
        Sample sample = new Sample();
        sample.setId(new SampleId(request.getS_id(), request.getS_stamp()));
        sample.setName(request.getName());
        sample.setWeightNet(request.getWeightNet());
        sample.setWeightBru(request.getWeightBru());
        sample.setWeightTar(request.getWeightTar());
        sample.setQuantity(request.getQuantity());
        sample.setDistance(request.getDistance());
        sample.setDateCrumbled(request.getDateCrumbled());
        sample.setSFlags(request.getSFlags());
        sample.setLane(request.getLane());
        sample.setComment(request.getComment());
        sample.setDateExported(request.getDateExported());
        return sample;
    }

    @Override
    public SampleResponse toResponse(Sample entity) {
        SampleResponse response = new SampleResponse();
        response.setS_id(entity.getId().getsId());
        response.setS_stamp(entity.getId().getsStamp());
        response.setName(entity.getName());
        response.setWeightNet(entity.getWeightNet());
        response.setWeightBru(entity.getWeightBru());
        response.setWeightTar(entity.getWeightTar());
        response.setQuantity(entity.getQuantity());
        response.setDistance(entity.getDistance());
        response.setDateCrumbled(entity.getDateCrumbled());
        response.setSFlags(entity.getSFlags());
        response.setLane(entity.getLane());
        response.setComment(entity.getComment());
        response.setDateExported(entity.getDateExported());

        // Computed field: boxposString
        response.setBoxposString(computeBoxposString(entity.getBoxPositions()));

        return response;
    }

    @Override
    public void updateEntity(Sample entity, SampleRequest request) {
        // Don't update ID - it's immutable
        entity.setName(request.getName());
        entity.setWeightNet(request.getWeightNet());
        entity.setWeightBru(request.getWeightBru());
        entity.setWeightTar(request.getWeightTar());
        entity.setQuantity(request.getQuantity());
        entity.setDistance(request.getDistance());
        entity.setDateCrumbled(request.getDateCrumbled());
        entity.setSFlags(request.getSFlags());
        entity.setLane(request.getLane());
        entity.setComment(request.getComment());
        entity.setDateExported(request.getDateExported());
    }

    private String computeBoxposString(Set<BoxPos> positions) {
        if (positions == null || positions.isEmpty()) {
            return "-";
        }
        var first = positions.iterator().next();
        var bid = first.getId().getBId();
        var pos = first.getId().getBposId();
        var sep = positions.size() == 1 ? "/" : "!";
        return bid + sep + pos;
    }
}
