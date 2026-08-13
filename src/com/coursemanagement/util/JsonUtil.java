package com.coursemanagement.util;

import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.dto.response.EnrollmentResponse;

import java.math.BigDecimal;
import java.util.List;

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

    public static BigDecimal getDecimal(String json, String key) {

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

        return new BigDecimal(json.substring(start, end));
    }

    public static <T extends Enum<T>> T getEnum(
            String json,
            String key,
            Class<T> enumClass) {

        String value = getString(json, key);

        if (value == null) {
            return null;
        }

        return Enum.valueOf(enumClass, value);
    }

    public static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
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

    public static String toJsonstudents(List<StudentResponse> students) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < students.size(); i++) {

            json.append(toJson(students.get(i)));

            if (i < students.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String toJson(CourseResponse course) {

        return """
                {
                  "id": %d,
                  "title": "%s",
                  "description": "%s",
                  "price": %s,
                  "capacity": %d,
                  "availableSeats": %d,
                  "status": "%s",
                  "createdAt": "%s",
                  "updatedAt": "%s"
                }
                """.formatted(
                course.getId(),
                escape(course.getTitle()),
                escape(course.getDescription()),
                course.getPrice(),
                course.getCapacity(),
                course.getAvailableSeats(),
                course.getStatus(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }

    public static String toJsoncourses(List<CourseResponse> courses) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < courses.size(); i++) {

            json.append(toJson(courses.get(i)));

            if (i < courses.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String toJson(EnrollmentResponse enrollment) {

        return """
            {
              "id": %d,
              "studentId": %d,
              "courseId": %d,
              "originalPrice": %s,
              "discountAmount": %s,
              "finalPrice": %s,
              "status": "%s",
              "enrollmentDate": "%s"
            }
            """.formatted(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getOriginalPrice(),
                enrollment.getDiscountAmount(),
                enrollment.getFinalPrice(),
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }

    public static String toJsonEnrollments(
            List<EnrollmentResponse> enrollments) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < enrollments.size(); i++) {

            json.append(toJson(enrollments.get(i)));

            if (i < enrollments.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String error(String message) {

        return """
                {
                  "error":"%s"
                }
                """.formatted(escape(message));
    }

    public static boolean isValidJson(String json) {

        if (json == null || json.isBlank()) {
            return false;
        }

        json = json.trim();

        if (!json.startsWith("{") || !json.endsWith("}")) {
            return false;
        }

        int quotes = 0;

        for (int i = 0; i < json.length(); i++) {
            if (json.charAt(i) == '"') {
                quotes++;
            }
        }

        return quotes % 2 == 0;
    }
}
