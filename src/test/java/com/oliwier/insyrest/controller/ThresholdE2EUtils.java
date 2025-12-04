package com.oliwier.insyrest.controller;

public class ThresholdE2EUtils {

    public static String buildValidJson(String thId, String valueMin, String valueMax, String dateChanged) {
        return """
            {
              "th_id": "%s",
              "value_min": %s,
              "value_max": %s,
              "date_changed": "%s"
            }
            """.formatted(thId, valueMin, valueMax, dateChanged);
    }

    public static String buildMinimalJson(String thId) {
        return """
            {
              "th_id": "%s"
            }
            """.formatted(thId);
    }

    public static String buildWithNullValues(String thId) {
        return """
            {
              "th_id": "%s",
              "value_min": null,
              "value_max": null,
              "date_changed": null
            }
            """.formatted(thId);
    }

    public static String buildWithoutOptionalFields(String thId, String valueMin, String valueMax) {
        return """
            {
              "th_id": "%s",
              "value_min": %s,
              "value_max": %s
            }
            """.formatted(thId, valueMin, valueMax);
    }
}
