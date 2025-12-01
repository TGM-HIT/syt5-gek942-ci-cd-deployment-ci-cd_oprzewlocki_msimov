package com.oliwier.insyrest.mapper;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic mapper interface for Entity <-> DTO conversion
 *
 * @param <ENTITY> The entity type
 * @param <REQUEST> The request DTO type (for create/update)
 * @param <RESPONSE> The response DTO type (for read operations)
 */
public interface EntityMapper<ENTITY, REQUEST, RESPONSE> {

    /**
     * Convert Request DTO to Entity (for POST operations)
     */
    ENTITY toEntity(REQUEST request);

    /**
     * Convert Entity to Response DTO (for GET operations)
     */
    RESPONSE toResponse(ENTITY entity);

    /**
     * Update existing entity with data from Request DTO (for PUT/PATCH operations)
     */
    void updateEntity(ENTITY entity, REQUEST request);

    /**
     * Convert list of entities to list of response DTOs
     */
    default List<RESPONSE> toResponseList(List<ENTITY> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert page of entities to page of response DTOs
     */
    default Page<RESPONSE> toResponsePage(Page<ENTITY> entityPage) {
        return entityPage.map(this::toResponse);
    }
}
