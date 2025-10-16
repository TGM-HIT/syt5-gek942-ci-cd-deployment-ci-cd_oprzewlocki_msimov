package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.Threshold;
import com.oliwier.insyrest.service.ThresholdService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thresholds")
public class ThresholdController extends AbstractCrudController<Threshold, String> {
    public ThresholdController(ThresholdService service, ObjectMapper objectMapper) {
        super(service, objectMapper);
    }
}
