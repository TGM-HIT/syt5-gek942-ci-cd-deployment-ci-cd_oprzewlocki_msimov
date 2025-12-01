package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.mapper.EntityMapper;
import com.oliwier.insyrest.service.CrudService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract CRUD controller that works with DTOs
 *
 * @param <ENTITY> Entity type
 * @param <ID> Entity ID type
 * @param <REQUEST> Request DTO type
 * @param <RESPONSE> Response DTO type
 */
@AllArgsConstructor
public abstract class AbstractCrudController<ENTITY, ID, REQUEST, RESPONSE> {

    protected final CrudService<ENTITY, ID> service;
    protected final EntityMapper<ENTITY, REQUEST, RESPONSE> mapper;

    @GetMapping
    public Page<RESPONSE> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam MultiValueMap<String, String> params
    ) {
        Map<String, String> filter = extractSubMap(params, "filter");
        Map<String, String> sort = extractSubMap(params, "sort");

        Page<ENTITY> entities = service.findAllWithFilters(PageRequest.of(page, size), filter, sort);

        return mapper.toResponsePage(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RESPONSE> getById(@PathVariable ID id) {
        Optional<ENTITY> entity = service.findById(id);
        return entity
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RESPONSE> create(@Valid @RequestBody REQUEST request) {
        ENTITY entity = mapper.toEntity(request);
        ENTITY saved = service.save(entity);
        RESPONSE response = mapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RESPONSE> update(
            @PathVariable ID id,
            @Valid @RequestBody REQUEST request
    ) {
        Optional<ENTITY> existingOpt = service.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ENTITY existing = existingOpt.get();
        mapper.updateEntity(existing, request);
        ENTITY updated = service.save(existing);
        RESPONSE response = mapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RESPONSE> patch(
            @PathVariable ID id,
            @RequestBody REQUEST request
    ) {
        Optional<ENTITY> existingOpt = service.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ENTITY existing = existingOpt.get();
        mapper.updateEntity(existing, request);
        ENTITY updated = service.save(existing);
        RESPONSE response = mapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        return service.findById(id)
                .<ResponseEntity<?>>map(entity -> {
                    try {
                        service.deleteById(id);
                        return ResponseEntity.noContent().build();
                    } catch (org.springframework.dao.DataIntegrityViolationException ex) {
                        String msg = ex.getMostSpecificCause().getMessage();
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of(
                                        "code", "FK_CONSTRAINT",
                                        "message", "Entity is still referenced",
                                        "detail", msg
                                ));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    protected Map<String, String> extractSubMap(MultiValueMap<String, String> params, String prefix) {
        Map<String, String> result = new HashMap<>();
        String start = prefix + "[";
        int startLen = start.length();

        for (String key : params.keySet()) {
            if (key.startsWith(start) && key.endsWith("]")) {
                String subKey = key.substring(startLen, key.length() - 1);
                result.put(subKey, params.getFirst(key));
            }
        }
        return result;
    }
}
