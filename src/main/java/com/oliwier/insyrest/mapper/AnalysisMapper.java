package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.AnalysisRequest;
import com.oliwier.insyrest.dto.response.AnalysisResponse;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnalysisMapper implements EntityMapper<Analysis, AnalysisRequest, AnalysisResponse> {

    private final SampleRepository sampleRepository;

    public AnalysisMapper(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Analysis toEntity(AnalysisRequest request) {
        Analysis analysis = new Analysis();

        if (request.getSId() != null && request.getSStamp() != null) {
            SampleId sampleId = new SampleId(request.getSId(), request.getSStamp());
            Sample sample = sampleRepository.findById(sampleId).orElse(null);
            analysis.setSample(sample);
        }

        analysis.setPol(request.getPol());
        analysis.setNat(request.getNat());
        analysis.setKal(request.getKal());
        analysis.setAn(request.getAn());
        analysis.setGlu(request.getGlu());
        analysis.setDry(request.getDry());
        analysis.setDateIn(request.getDateIn());
        analysis.setDateOut(request.getDateOut());
        analysis.setWeightMea(request.getWeightMea());
        analysis.setWeightNrm(request.getWeightNrm());
        analysis.setWeightCur(request.getWeightCur());
        analysis.setWeightDif(request.getWeightDif());
        analysis.setDensity(request.getDensity());
        analysis.setAFlags(request.getAFlags());
        analysis.setLane(request.getLane());
        analysis.setComment(request.getComment());
        analysis.setDateExported(request.getDateExported());

        return analysis;
    }

    @Override
    public AnalysisResponse toResponse(Analysis entity) {
        AnalysisResponse response = new AnalysisResponse();

        response.setAId(entity.getAId());

        if (entity.getSample() != null) {
            response.setSId(entity.getSample().getId().getsId());
            response.setSStamp(entity.getSample().getId().getsStamp());
            response.setBoxposString(computeBoxposString(entity.getSample().getBoxPositions()));
        } else {
            response.setBoxposString("-");
        }

        response.setPol(entity.getPol());
        response.setNat(entity.getNat());
        response.setKal(entity.getKal());
        response.setAn(entity.getAn());
        response.setGlu(entity.getGlu());
        response.setDry(entity.getDry());
        response.setDateIn(entity.getDateIn());
        response.setDateOut(entity.getDateOut());
        response.setWeightMea(entity.getWeightMea());
        response.setWeightNrm(entity.getWeightNrm());
        response.setWeightCur(entity.getWeightCur());
        response.setWeightDif(entity.getWeightDif());
        response.setDensity(entity.getDensity());
        response.setAFlags(entity.getAFlags());
        response.setLane(entity.getLane());
        response.setComment(entity.getComment());
        response.setDateExported(entity.getDateExported());

        return response;
    }

    @Override
    public void updateEntity(Analysis entity, AnalysisRequest request) {
        if (request.getSId() != null && request.getSStamp() != null) {
            SampleId sampleId = new SampleId(request.getSId(), request.getSStamp());
            Sample sample = sampleRepository.findById(sampleId).orElse(null);
            entity.setSample(sample);
        }

        entity.setPol(request.getPol());
        entity.setNat(request.getNat());
        entity.setKal(request.getKal());
        entity.setAn(request.getAn());
        entity.setGlu(request.getGlu());
        entity.setDry(request.getDry());
        entity.setDateIn(request.getDateIn());
        entity.setDateOut(request.getDateOut());
        entity.setWeightMea(request.getWeightMea());
        entity.setWeightNrm(request.getWeightNrm());
        entity.setWeightCur(request.getWeightCur());
        entity.setWeightDif(request.getWeightDif());
        entity.setDensity(request.getDensity());
        entity.setAFlags(request.getAFlags());
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
