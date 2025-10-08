package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/box")
public class BoxController extends AbstractCrudController<Box, String>{
    protected BoxController(CrudService<Box, String> service) {
        super(service);
    }
}
