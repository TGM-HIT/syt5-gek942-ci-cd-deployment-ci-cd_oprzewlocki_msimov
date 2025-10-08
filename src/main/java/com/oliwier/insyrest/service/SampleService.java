package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SampleService {

    private final SampleRepository repository;

    public SampleService(SampleRepository repository) {
        this.repository = repository;
    }

    public Page<Sample> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Sample> findById(String sId, LocalDateTime sStamp) {
        SampleId key = new SampleId(sId, sStamp);
        return repository.findById(key);
    }

    public Sample save(Sample sample) {
        return repository.save(sample);
    }

    public void deleteById(String sId, LocalDateTime sStamp) {
        SampleId key = new SampleId(sId, sStamp);
        repository.deleteById(key);
    }
}
