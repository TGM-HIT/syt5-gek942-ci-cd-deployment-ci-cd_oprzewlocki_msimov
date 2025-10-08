package com.oliwier.insyrest.repository;

import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.BoxPosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxPosRepository extends JpaRepository<BoxPos, BoxPosId> {
}