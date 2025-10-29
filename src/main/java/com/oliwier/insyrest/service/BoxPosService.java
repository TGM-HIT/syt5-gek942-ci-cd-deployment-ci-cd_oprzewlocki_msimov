package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.BoxPosId;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.repository.BoxPosRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoxPosService extends AbstractCrudService<BoxPos, BoxPosId>{
    private final BoxPosRepository boxPosRepository;
    private final SampleRepository sampleRepository;



    protected BoxPosService(BoxPosRepository repository, BoxPosRepository boxPosRepository, SampleRepository sampleRepository) {
        super(repository, repository);
        this.boxPosRepository = boxPosRepository;
        this.sampleRepository = sampleRepository;
    }

    @Transactional
    public void deleteAllBySample(SampleId id) {
        sampleRepository.findById(id).ifPresent(boxPosRepository::deleteAllBySample);
    }

    @Transactional(readOnly = true)
    public boolean existsForSample(SampleId id) {
        return sampleRepository.findById(id)
                .map(s -> !boxPosRepository.findBySample(s).isEmpty())
                .orElse(false);
    }
}
