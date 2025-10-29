package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends JpaRepository<Box, String>, JpaSpecificationExecutor<Box> {
}
