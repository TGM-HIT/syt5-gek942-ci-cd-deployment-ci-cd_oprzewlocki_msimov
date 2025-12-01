package com.oliwier.insyrest.controller.analysis;

public class AnalysisE2EUtils {

    public static String buildValidJson(String sId, String sStamp, String timestamp) {
        return """
            {
              "sId": %s,
              "sStamp": %s,
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
              "comment": "E2E Analysis Test",
              "aFlags": "-",
              "dateExported": "%s"
            }
            """.formatted(
                (sId == null ? "null" : "\"" + sId + "\""),
                (sStamp == null ? "null" : "\"" + sStamp + "\""),
                timestamp, timestamp, timestamp
        );
    }
}
