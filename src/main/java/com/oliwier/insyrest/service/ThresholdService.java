package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ThresholdService extends AbstractCrudService<Threshold, String> {
    protected ThresholdService(JpaRepository<Threshold, String> repository) {
        super(repository);
    }
}
