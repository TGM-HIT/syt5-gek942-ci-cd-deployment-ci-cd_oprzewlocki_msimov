package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public class LogService extends AbstractCrudService<Log, Long>{

    protected LogService(JpaRepository<Log, Long> repository) {
        super(repository);
    }
}
