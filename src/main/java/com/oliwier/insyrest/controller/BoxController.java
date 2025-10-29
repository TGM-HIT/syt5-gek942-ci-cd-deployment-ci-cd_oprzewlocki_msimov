package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.service.BoxService;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/box")
public class BoxController extends AbstractCrudController<Box, String>{

    private final BoxService boxService;


    protected BoxController(CrudService<Box, String> service, ObjectMapper objectMapper, BoxService boxService) {
        super(service, objectMapper);
        this.boxService = boxService;
    }

    @DeleteMapping("/advanced/{bId}")
    @Transactional
    public ResponseEntity<?> advancedDeleteBox(
            @PathVariable String bId,
            @RequestParam(required = false) String action
    ) {

        // Step 1: Check only
        if ("check".equalsIgnoreCase(action)) {
            boolean hasBoxPos = boxService.hasBoxPos(bId);

            if (hasBoxPos) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "type", "boxpos",
                                "message", "This box still contains samples."
                        ));
            }

            return ResponseEntity.ok(Map.of("type", "none"));
        }

        // Step 2: Cascade delete
        if ("cascade".equalsIgnoreCase(action)) {
            boxService.cascadeDeleteBox(bId);
            return ResponseEntity.ok(Map.of("message", "Box and its box positions deleted."));
        }

        return ResponseEntity.badRequest().body("Invalid action");
    }
}
