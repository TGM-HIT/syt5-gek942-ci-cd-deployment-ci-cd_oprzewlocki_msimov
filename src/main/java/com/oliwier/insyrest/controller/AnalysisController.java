package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.service.AnalysisService;
import com.oliwier.insyrest.service.CrudService;
import com.oliwier.insyrest.service.SampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController extends AbstractCrudController<Analysis, Long> {
    private final SampleService sampleService;


    protected AnalysisController(AnalysisService service, ObjectMapper objectMapper, SampleService sampleService) {
        super(service, objectMapper);
        this.sampleService = sampleService;
    }

    @GetMapping("/validate-sample")
    public ResponseEntity<?> validateSample(
            @RequestParam String s_id,
            @RequestParam String s_stamp
    ) {
        try {
            LocalDateTime stamp = LocalDateTime.parse(s_stamp); // ← convert string to LocalDateTime
            SampleId id = new SampleId(s_id, stamp);
            boolean exists = sampleService.findById(id).isPresent();
            return ResponseEntity.ok(Map.of("valid", exists));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid timestamp format"));
        }
    }

}
