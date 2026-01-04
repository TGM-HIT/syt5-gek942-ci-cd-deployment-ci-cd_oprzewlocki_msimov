package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoxRepository extends JpaRepository<Box, String>, JpaSpecificationExecutor<Box> {
    List<Box> findAllByDateExportedBetween(LocalDateTime start, LocalDateTime end);
}
