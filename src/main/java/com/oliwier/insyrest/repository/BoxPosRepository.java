package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoxPosRepository extends JpaRepository<BoxPos, BoxPosId>, JpaSpecificationExecutor<BoxPos> {
    @Transactional
    void deleteAllBySample(Sample sample);

    @Query("""
    SELECT COUNT(bp) > 0
    FROM BoxPos bp
    WHERE bp.sample.id.sId = :#{#id.sId}
      AND bp.sample.id.sStamp = :#{#id.sStamp}
    """)
    boolean existsBySampleId(@Param("id") SampleId id);


    long countByBox(Box box);


    List<BoxPos> findBySample(Sample sample);
}