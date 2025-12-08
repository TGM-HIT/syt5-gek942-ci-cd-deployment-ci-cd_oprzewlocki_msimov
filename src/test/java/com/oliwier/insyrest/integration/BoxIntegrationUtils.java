package com.oliwier.insyrest.integration;

public class BoxIntegrationUtils {

    public static String buildValidJson(String bId) {
        return """
            {
              "bid": "%s",
              "name": "E2E Test Box",
              "numMax": 100,
              "type": 1,
              "comment": "Test box for E2E testing"
            }
            """.formatted(bId);
    }

    public static String buildMinimalJson(String bId) {
        return """
            {
              "bid": "%s"
            }
            """.formatted(bId);
    }
}
