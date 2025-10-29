package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.repository.AnalysisRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnalysisService extends AbstractCrudService<Analysis, Long>{
    private final AnalysisRepository analysisRepository;
    private final SampleRepository sampleRepository;


    protected AnalysisService(AnalysisRepository repository, AnalysisRepository analysisRepository, SampleRepository sampleRepository) {
        super(repository, repository);
        this.analysisRepository = analysisRepository;
        this.sampleRepository = sampleRepository;
    }

    @Transactional
    public void deleteAllBySample(SampleId id) {
        sampleRepository.findById(id).ifPresent(analysisRepository::deleteAllBySample);
    }

    @Transactional
    public void detachSampleReferences(SampleId id) {
        sampleRepository.findById(id).ifPresent(sample -> {
            List<Analysis> analyses = analysisRepository.findBySample(sample);
            for (Analysis a : analyses) {
                a.setSample(null);
            }
            analysisRepository.saveAll(analyses);
        });
    }

    @Transactional(readOnly = true)
    public boolean existsForSample(SampleId id) {
        return analysisRepository.existsBySampleId(id);
    }

}
