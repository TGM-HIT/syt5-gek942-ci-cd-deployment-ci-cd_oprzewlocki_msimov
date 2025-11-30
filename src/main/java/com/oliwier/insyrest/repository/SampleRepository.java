package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, SampleId>, JpaSpecificationExecutor<Sample> {
}

