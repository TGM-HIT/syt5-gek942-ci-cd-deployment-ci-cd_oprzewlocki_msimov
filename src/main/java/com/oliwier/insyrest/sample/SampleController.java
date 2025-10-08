package com.oliwier.insyrest.sample;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/samples")
public class SampleController {

    private final SampleService service;

    public SampleController(SampleService service) {
        this.service = service;
    }

    // GET all mit Pagination
    @GetMapping
    public Page<Sample> getAllSamples(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return service.findAll(PageRequest.of(page, size));
    }

    // GET by composite key
    @GetMapping("/{id}/{stamp}")
    public ResponseEntity<Sample> getSample(
            @PathVariable String id,
            @PathVariable String stamp) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime parsedStamp = LocalDateTime.parse(stamp, formatter);

        return service.findById(id, parsedStamp)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public Sample createSample(@RequestBody Sample sample) {
        return service.save(sample);
    }

    // PUT
    @PutMapping("/{id}/{stamp}")
    public ResponseEntity<Sample> updateSample(
            @PathVariable String id,
            @PathVariable String stamp,
            @RequestBody Sample updated) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime parsedStamp = LocalDateTime.parse(stamp, formatter);

        return service.findById(id, parsedStamp)
                .map(existing -> {
                    updated.setId(existing.getId()); // PK fix beibehalten
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}/{stamp}")
    public ResponseEntity<Void> deleteSample(
            @PathVariable String id,
            @PathVariable String stamp) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime parsedStamp = LocalDateTime.parse(stamp, formatter);

        if (service.findById(id, parsedStamp).isPresent()) {
            service.deleteById(id, parsedStamp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
