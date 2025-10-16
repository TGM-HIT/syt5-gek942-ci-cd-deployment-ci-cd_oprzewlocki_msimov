package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.BoxPosId;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boxpos")
public class BoxPosController extends AbstractCrudController<BoxPos, BoxPosId>{
    protected BoxPosController(CrudService<BoxPos, BoxPosId> service, ObjectMapper objectMapper) {
        super(service, objectMapper);
    }
}
