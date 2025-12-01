// src/main/java/com/oliwier/insyrest/controller/AnalysisController.java
package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.request.AnalysisRequest;
import com.oliwier.insyrest.dto.response.AnalysisResponse;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.mapper.AnalysisMapper;
import com.oliwier.insyrest.service.AnalysisService;
import com.oliwier.insyrest.service.SampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController extends AbstractCrudController<Analysis, Long, AnalysisRequest, AnalysisResponse> {

    private final SampleService sampleService;

    public AnalysisController(
            AnalysisService analysisService,
            AnalysisMapper analysisMapper,
            SampleService sampleService
    ) {
        super(analysisService, analysisMapper);
        this.sampleService = sampleService;
    }

    @GetMapping("/validate-sample")
    public ResponseEntity<?> validateSample(
            @RequestParam String s_id,
            @RequestParam String s_stamp
    ) {
        try {
            LocalDateTime stamp = LocalDateTime.parse(s_stamp);
            SampleId id = new SampleId(s_id, stamp);
            boolean exists = sampleService.findById(id).isPresent();
            return ResponseEntity.ok(Map.of("valid", exists));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid timestamp format"));
        }
    }
}
