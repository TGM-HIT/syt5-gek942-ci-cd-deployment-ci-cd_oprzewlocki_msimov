package com.oliwier.insyrest.integration;

import java.util.concurrent.atomic.AtomicInteger;

public class ThresholdIntegrationUtils {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static String generateShortThId() {
        return "TH" + counter.incrementAndGet();
    }

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
