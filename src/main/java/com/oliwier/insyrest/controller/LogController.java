package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController extends AbstractCrudController<Log, Long> {

    protected LogController(CrudService<Log, Long> service, ObjectMapper objectMapper) {
        super(service, objectMapper);
    }
}

