package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BoxService extends AbstractCrudService<Box, String>{
    protected BoxService(JpaRepository<Box, String> repository) {
        super(repository);
    }
}
