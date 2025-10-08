package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.BoxPosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BoxPosService extends AbstractCrudService<BoxPos, BoxPosId>{
    protected BoxPosService(JpaRepository<BoxPos, BoxPosId> repository) {
        super(repository);
    }
}
