package com.oliwier.insyrest.controller;

public class BoxPosE2EUtils {

    public static String buildValidJson(int bposId, String bId, String sId, String sStamp, String dateExported) {
        return """
            {
              "bposId": %d,
              "bId": "%s",
              "sId": "%s",
              "sStamp": "%s",
              "dateExported": %s
            }
            """.formatted(bposId, bId, sId, sStamp, dateExported == null ? "null" : "\"" + dateExported + "\"");
    }

    public static String buildMinimalJson(int bposId, String bId, String sId, String sStamp) {
        return """
            {
              "bposId": %d,
              "bId": "%s",
              "sId": "%s",
              "sStamp": "%s"
            }
            """.formatted(bposId, bId, sId, sStamp);
    }

    public static String buildWithoutDateExported(int bposId, String bId, String sId, String sStamp) {
        return buildMinimalJson(bposId, bId, sId, sStamp);
    }
}
