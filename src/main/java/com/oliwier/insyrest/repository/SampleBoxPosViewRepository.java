package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.SampleBoxPosView;
import com.oliwier.insyrest.entity.id.SampleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoxPosViewRepository
        extends JpaRepository<SampleBoxPosView, SampleId>,
        JpaSpecificationExecutor<SampleBoxPosView> {
}

