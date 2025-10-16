package com.oliwier.insyrest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.service.CrudService;

import com.oliwier.insyrest.service.SampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/samples")
public class SampleController extends AbstractCrudController<Sample, SampleId> {
    final SampleService sampleService;
    protected SampleController(SampleService service, ObjectMapper objectMapper) {
        super(service, objectMapper);
        sampleService = service;
    }

    @GetMapping("/{id}/boxpositions")
    public ResponseEntity<Set<BoxPos>> getBoxPositions(@PathVariable SampleId id) {
        return sampleService.findById(id)
                .map(sample -> ResponseEntity.ok(sample.getBoxPositions()))
                .orElse(ResponseEntity.notFound().build());
    }
}
