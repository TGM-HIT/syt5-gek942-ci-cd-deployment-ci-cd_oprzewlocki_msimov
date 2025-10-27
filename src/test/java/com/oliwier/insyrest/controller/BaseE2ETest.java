package com.oliwier.insyrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseE2ETest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate rest;

    protected String baseUrl;

    protected String uniqueId() {
        return UUID.randomUUID().toString().substring(0, 13);
    }

    protected String timestamp() {
        return LocalDateTime.now().toString();
    }

    protected String buildValidJson(String sampleId) {
        String now = timestamp();
        return """
        {
          "sample": {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "Valid Sample",
              "weightNet": 10.0,
              "weightBru": 12.0,
              "weightTar": 2.0,
              "quantity": 1,
              "distance": 0.0,
              "dateCrumbled": "%s",
              "sFlags": "-----",
              "lane": 0,
              "comment": "inserted by E2E",
              "dateExported": "%s"
          },
          "pol": 1.0,
          "nat": 1.0,
          "kal": 1.0,
          "an": 1.0,
          "glu": 1.0,
          "dry": 1.0,
          "dateIn": "%s",
          "dateOut": "%s",
          "weightMea": 1.00,
          "weightNrm": 1.00,
          "weightCur": 1.00,
          "weightDif": 0.00,
          "density": 1.00,
          "lane": 1,
          "comment": "E2E creation test",
          "aFlags": "-",
          "dateExported": "%s"
        }
        """.formatted(sampleId, now, now, now, now, now, now);
    }

    protected String baseUrl() {
        return "http://localhost:" + port + "/api/analysis";
    }
}
