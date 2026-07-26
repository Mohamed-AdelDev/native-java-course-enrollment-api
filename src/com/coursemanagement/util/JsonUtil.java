package com.coursemanagement.util;
import com.coursemanagement.dto.response.StudentResponse;

public class JsonUtil {

    public static String getString(String json, String key) {

        String search = "\"" + key + "\":";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (json.charAt(start) != '"') {
            return null;
        }

        start++;

        int end = json.indexOf("\"", start);

        if (end == -1) {
            return null;
        }

        return json.substring(start, end);
    }

    public static Integer getInt(String json, String key) {

        String search = "\"" + key + "\":";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        int end = start;

        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }

        return Integer.parseInt(json.substring(start, end));
    }
    public static Double getDouble(String json, String key) {

        String search = "\"" + key + "\":";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        int end = start;

        while (end < json.length()
                && (Character.isDigit(json.charAt(end))
                || json.charAt(end) == '.')) {

            end++;
        }

        return Double.parseDouble(json.substring(start, end));
    }
    public static <T extends Enum<T>> T getEnum(String json, String key, Class<T> enumClass) {

        String value = getString(json, key);

        if (value == null) {
            return null;
        }

        return Enum.valueOf(enumClass, value);
    }

    public static String toJson(StudentResponse student) {

        return """
            {
              "id": %d,
              "fullName": "%s",
              "email": "%s",
              "role": "%s",
              "active": %b,
              "createdAt": "%s"
            }
            """.formatted(
                student.getId(),
                escape(student.getFullName()),
                escape(student.getEmail()),
                student.getRole(),
                student.isActive(),
                student.getCreatedAt()
        );
    }
    public static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}