package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/box")
public class BoxController extends AbstractCrudController<Box, String>{
    protected BoxController(CrudService<Box, String> service, ObjectMapper objectMapper) {
        super(service, objectMapper);
    }
}
