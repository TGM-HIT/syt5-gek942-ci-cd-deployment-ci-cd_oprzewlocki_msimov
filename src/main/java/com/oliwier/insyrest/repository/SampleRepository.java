package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.SampleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, SampleId> {
}

