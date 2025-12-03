package com.oliwier.insyrest.controller;

public class SampleE2EUtils {

    public static String buildValidJson(String sId, String sStamp, String timestamp) {
        return """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "E2E Sample",
              "weightNet": 10.0,
              "weightBru": 12.0,
              "weightTar": 2.0,
              "quantity": 1,
              "distance": 0.0,
              "dateCrumbled": %s,
              "sflags": "-----",
              "lane": 0,
              "comment": "Created for test",
              "dateExported": %s
            }
            """.formatted(sId, sStamp, timestamp == null ? "null" : "\"" + timestamp + "\"", timestamp == null ? "null" : "\"" + timestamp + "\"");
    }
}
