package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.request.SampleRequest;
import com.oliwier.insyrest.dto.response.SampleResponse;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.mapper.SampleMapper;
import com.oliwier.insyrest.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/samples")
public class SampleController extends AbstractCrudController<Sample, SampleId, SampleRequest, SampleResponse> {

    private final SampleService sampleService;
    private final AnalysisService analysisService;
    private final BoxPosService boxPosService;
    private final BoxService boxService;

    public SampleController(
            SampleService sampleService,
            SampleMapper sampleMapper,
            AnalysisService analysisService,
            BoxPosService boxPosService,
            BoxService boxService
    ) {
        super(sampleService, sampleMapper);
        this.sampleService = sampleService;
        this.analysisService = analysisService;
        this.boxPosService = boxPosService;
        this.boxService = boxService;
    }

    @DeleteMapping("/advanced/{id}")
    @Transactional
    public ResponseEntity<?> advancedDelete(
            @PathVariable("id") SampleId id,
            @RequestParam String action
    ) {
        boolean hasAnalyses = analysisService.existsForSample(id);
        boolean hasBoxpos = boxPosService.existsForSample(id);
        boolean hasBoxes = boxService.existsLinkedToSample(id);

        if ("check".equalsIgnoreCase(action)) {
            if (hasBoxes && hasBoxpos && hasAnalyses) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("type", "all", "message",
                                "Sample is linked to analyses, box positions, and boxes."));
            }
            if (hasBoxes || hasBoxpos) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("type", "boxpos", "message",
                                "Sample is placed inside a Box/BoxPos — cannot detach."));
            }
            if (hasAnalyses) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("type", "analysis", "message",
                                "Sample is still referenced by analyses."));
            }
            return ResponseEntity.ok(Map.of("type", "none"));
        }

        switch (action.toLowerCase()) {
            case "cascade" -> {
                analysisService.deleteAllBySample(id);
                boxPosService.deleteAllBySample(id);
                boxService.deleteBoxesBySample(id);
                boxService.deleteEmptyBoxes();
                sampleService.deleteById(id);

                return ResponseEntity.ok(Map.of(
                        "message", "Sample, analyses, boxes, and box positions deleted"
                ));
            }

            case "detach" -> {
                analysisService.detachSampleReferences(id);
                boxService.deleteBoxesBySample(id);
                boxService.deleteEmptyBoxes();
                sampleService.deleteById(id);

                return ResponseEntity.ok(Map.of(
                        "message", "Analyses detached, boxes deleted, and sample removed"
                ));
            }

            default -> {
                return ResponseEntity.badRequest().body(Map.of("error", "Unknown action"));
            }
        }
    }
}
