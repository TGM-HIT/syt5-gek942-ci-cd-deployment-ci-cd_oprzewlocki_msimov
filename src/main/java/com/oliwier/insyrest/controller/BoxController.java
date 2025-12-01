// src/main/java/com/oliwier/insyrest/controller/BoxController.java
package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.request.BoxRequest;
import com.oliwier.insyrest.dto.response.BoxResponse;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.mapper.BoxMapper;
import com.oliwier.insyrest.service.BoxService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boxes")
public class BoxController extends AbstractCrudController<Box, String, BoxRequest, BoxResponse> {
    public BoxController(BoxService boxService, BoxMapper boxMapper) {
        super(boxService, boxMapper);
    }
}
