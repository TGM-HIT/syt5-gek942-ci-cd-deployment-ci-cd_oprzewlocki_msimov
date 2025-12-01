package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.request.ThresholdRequest;
import com.oliwier.insyrest.dto.response.ThresholdResponse;
import com.oliwier.insyrest.entity.Threshold;
import com.oliwier.insyrest.mapper.ThresholdMapper;
import com.oliwier.insyrest.service.ThresholdService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thresholds")
public class ThresholdController extends AbstractCrudController<Threshold, String, ThresholdRequest, ThresholdResponse> {

    public ThresholdController(ThresholdService thresholdService, ThresholdMapper thresholdMapper) {
        super(thresholdService, thresholdMapper);
    }
}
