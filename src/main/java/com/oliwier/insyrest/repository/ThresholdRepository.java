package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, String>, JpaSpecificationExecutor<Threshold> {
}