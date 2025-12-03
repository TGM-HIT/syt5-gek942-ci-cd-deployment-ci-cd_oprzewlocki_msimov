package com.oliwier.insyrest.controller;

public class BoxE2EUtils {

    public static String buildValidJson(String bId) {
        return """
            {
              "b_id": "%s",
              "name": "E2E Test Box",
              "num_max": 100,
              "type": 1,
              "comment": "Test box for E2E testing"
            }
            """.formatted(bId);
    }

    public static String buildMinimalJson(String bId) {
        return """
            {
              "b_id": "%s"
            }
            """.formatted(bId);
    }
}
