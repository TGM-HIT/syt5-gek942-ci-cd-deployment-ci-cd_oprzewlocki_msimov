package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long>, JpaSpecificationExecutor<Analysis> {
    void deleteAllBySample(Sample sample);
    List<Analysis> findBySample(Sample sample);

    @Query("""
        SELECT COUNT(a) > 0
        FROM Analysis a
        WHERE a.sample.id.sId = :#{#id.sId}
          AND a.sample.id.sStamp = :#{#id.sStamp}
    """)
    boolean existsBySampleId(@Param("id") SampleId id);

}
