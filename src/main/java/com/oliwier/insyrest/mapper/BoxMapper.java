package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.BoxRequest;
import com.oliwier.insyrest.dto.response.BoxResponse;
import com.oliwier.insyrest.entity.Box;
import org.springframework.stereotype.Component;

@Component
public class BoxMapper implements EntityMapper<Box, BoxRequest, BoxResponse> {

    @Override
    public Box toEntity(BoxRequest request) {
        Box box = new Box();
        box.setBId(request.getBId());
        box.setName(request.getName());
        box.setNumMax(request.getNumMax());
        box.setType(request.getType());
        box.setComment(request.getComment());
        box.setDateExported(request.getDateExported());
        return box;
    }

    @Override
    public BoxResponse toResponse(Box entity) {
        BoxResponse response = new BoxResponse();
        response.setBId(entity.getBId());
        response.setName(entity.getName());
        response.setNumMax(entity.getNumMax());
        response.setType(entity.getType());
        response.setComment(entity.getComment());
        response.setDateExported(entity.getDateExported());
        return response;
    }

    @Override
    public void updateEntity(Box entity, BoxRequest request) {
        // Don't update bId - it's the primary key
        entity.setName(request.getName());
        entity.setNumMax(request.getNumMax());
        entity.setType(request.getType());
        entity.setComment(request.getComment());
        entity.setDateExported(request.getDateExported());
    }
}
