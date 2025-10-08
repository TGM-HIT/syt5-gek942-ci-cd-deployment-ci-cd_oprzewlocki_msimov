package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController extends AbstractCrudController<Analysis, Long> {
    protected AnalysisController(CrudService<Analysis, Long> service) {
        super(service);
    }
}
