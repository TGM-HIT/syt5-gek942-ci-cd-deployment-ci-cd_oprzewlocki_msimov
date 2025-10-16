package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.repository.AnalysisRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalysisService extends AbstractCrudService<Analysis, Long>{
    protected AnalysisService(AnalysisRepository repository) {
        super(repository);
    }
}
