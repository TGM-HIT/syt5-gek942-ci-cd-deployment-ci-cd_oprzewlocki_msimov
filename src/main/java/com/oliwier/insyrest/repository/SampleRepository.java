package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<Sample, SampleId>, JpaSpecificationExecutor<Sample> {

    @Query("SELECT s FROM Sample s WHERE s.id.sStamp BETWEEN :start AND :end")
    List<Sample> findSamplesInTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}