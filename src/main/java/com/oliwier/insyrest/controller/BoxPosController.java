// src/main/java/com/oliwier/insyrest/controller/BoxPosController.java
package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.request.BoxPosRequest;
import com.oliwier.insyrest.dto.response.BoxPosResponse;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.mapper.BoxPosMapper;
import com.oliwier.insyrest.service.BoxPosService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boxpos")
public class BoxPosController extends AbstractCrudController<BoxPos, BoxPosId, BoxPosRequest, BoxPosResponse> {
    public BoxPosController(BoxPosService boxPosService, BoxPosMapper boxPosMapper) {
        super(boxPosService, boxPosMapper);
    }
}
