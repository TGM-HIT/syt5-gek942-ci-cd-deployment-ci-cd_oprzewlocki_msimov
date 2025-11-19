package com.oliwier.insyrest.service;

import com.oliwier.insyrest.dto.SampleDTO;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SampleService extends AbstractCrudService<Sample, SampleId> {

    protected SampleService(SampleRepository repository) {
        super(repository, repository);
    }
}
