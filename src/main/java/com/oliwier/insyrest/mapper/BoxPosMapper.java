package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.BoxPosRequest;
import com.oliwier.insyrest.dto.response.BoxPosResponse;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.stereotype.Component;

@Component
public class BoxPosMapper implements EntityMapper<BoxPos, BoxPosRequest, BoxPosResponse> {

    private final BoxRepository boxRepository;
    private final SampleRepository sampleRepository;

    public BoxPosMapper(BoxRepository boxRepository, SampleRepository sampleRepository) {
        this.boxRepository = boxRepository;
        this.sampleRepository = sampleRepository;
    }

    @Override
    public BoxPos toEntity(BoxPosRequest request) {
        BoxPosId id = new BoxPosId(request.getBposId(), request.getBId());

        Box box = boxRepository.findById(request.getBId())
                .orElseThrow(() -> new RuntimeException("Box not found: " + request.getBId()));

        SampleId sampleId = new SampleId(request.getSId(), request.getSStamp());
        Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(() -> new RuntimeException("Sample not found: " + request.getSId() + "," + request.getSStamp()));

        return new BoxPos(id, box, sample, request.getDateExported());
    }

    @Override
    public BoxPosResponse toResponse(BoxPos entity) {
        BoxPosResponse response = new BoxPosResponse();
        response.setBposId(entity.getId().getBposId());
        response.setBId(entity.getId().getBId());
        response.setSId(entity.getSample().getId().getsId());
        response.setSStamp(entity.getSample().getId().getsStamp());
        response.setDateExported(entity.getDateExported());
        return response;
    }

    @Override
    public void updateEntity(BoxPos entity, BoxPosRequest request) {
        // Update box reference if changed
        Box box = boxRepository.findById(request.getBId())
                .orElseThrow(() -> new RuntimeException("Box not found: " + request.getBId()));
        entity.setBox(box);

        // Update sample reference if changed
        SampleId sampleId = new SampleId(request.getSId(), request.getSStamp());
        Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(() -> new RuntimeException("Sample not found: " + request.getSId() + "," + request.getSStamp()));
        entity.setSample(sample);

        entity.setDateExported(request.getDateExported());
    }
}
