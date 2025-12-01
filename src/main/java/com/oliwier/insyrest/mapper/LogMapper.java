package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.LogRequest;
import com.oliwier.insyrest.dto.response.LogResponse;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.AnalysisRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.stereotype.Component;

@Component
public class LogMapper implements EntityMapper<Log, LogRequest, LogResponse> {

    private final SampleRepository sampleRepository;
    private final AnalysisRepository analysisRepository;

    public LogMapper(SampleRepository sampleRepository, AnalysisRepository analysisRepository) {
        this.sampleRepository = sampleRepository;
        this.analysisRepository = analysisRepository;
    }

    @Override
    public Log toEntity(LogRequest request) {
        Log log = new Log();
        log.setDateCreated(request.getDateCreated());
        log.setLevel(request.getLevel());
        log.setInfo(request.getInfo());
        log.setDateExported(request.getDateExported());

        // Set sample reference if provided
        if (request.getSId() != null && request.getSStamp() != null) {
            SampleId sampleId = new SampleId(request.getSId(), request.getSStamp());
            Sample sample = sampleRepository.findById(sampleId).orElse(null);
            log.setSample(sample);
        }

        // Set analysis reference if provided
        if (request.getAId() != null) {
            Analysis analysis = analysisRepository.findById(request.getAId()).orElse(null);
            log.setAnalysis(analysis);
        }

        return log;
    }

    @Override
    public LogResponse toResponse(Log entity) {
        LogResponse response = new LogResponse();
        response.setLogId(entity.getLogId());
        response.setDateCreated(entity.getDateCreated());
        response.setLevel(entity.getLevel());
        response.setInfo(entity.getInfo());
        response.setDateExported(entity.getDateExported());

        if (entity.getSample() != null) {
            response.setSId(entity.getSample().getId().getsId());
            response.setSStamp(entity.getSample().getId().getsStamp());
        }

        if (entity.getAnalysis() != null) {
            response.setAId(entity.getAnalysis().getAId());
        }

        return response;
    }

    @Override
    public void updateEntity(Log entity, LogRequest request) {
        entity.setDateCreated(request.getDateCreated());
        entity.setLevel(request.getLevel());
        entity.setInfo(request.getInfo());
        entity.setDateExported(request.getDateExported());

        // Update sample reference if provided
        if (request.getSId() != null && request.getSStamp() != null) {
            SampleId sampleId = new SampleId(request.getSId(), request.getSStamp());
            Sample sample = sampleRepository.findById(sampleId).orElse(null);
            entity.setSample(sample);
        }

        // Update analysis reference if provided
        if (request.getAId() != null) {
            Analysis analysis = analysisRepository.findById(request.getAId()).orElse(null);
            entity.setAnalysis(analysis);
        }
    }
}
