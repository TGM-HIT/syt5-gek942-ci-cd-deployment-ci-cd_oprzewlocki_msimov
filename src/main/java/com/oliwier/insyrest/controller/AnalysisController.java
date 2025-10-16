package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.service.AnalysisService;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController extends AbstractCrudController<Analysis, Long> {
    protected AnalysisController(AnalysisService service, ObjectMapper objectMapper) {
        super(service, objectMapper);
    }
}
