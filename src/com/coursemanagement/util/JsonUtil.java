package com.coursemanagement.util;

import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.exception.ErrorResponse;
import com.coursemanagement.model.AuditLog;

import java.math.BigDecimal;
import java.util.List;

public class JsonUtil {

    public static String getString(String json, String key) {

        if (json == null || key == null) {
            return null;
        }

        String search = "\"" + key + "\":";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (start < json.length()
                && Character.isWhitespace(json.charAt(start))) {

            start++;
        }

        if (start >= json.length()
                || json.charAt(start) != '"') {

            return null;
        }

        start++;

        int end = start;

        while (end < json.length()) {

            if (json.charAt(end) == '"'
                    && json.charAt(end - 1) != '\\') {

                break;
            }

            end++;
        }

        if (end >= json.length()) {
            return null;
        }

        return json.substring(start, end);
    }

    public static Integer getInt(
            String json,
            String key) {

        if (json == null || key == null) {
            return null;
        }

        String search = "\"" + key + "\":";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (start < json.length()
                && Character.isWhitespace(json.charAt(start))) {

            start++;
        }

        int end = start;

        if (end < json.length()
                && json.charAt(end) == '-') {

            end++;
        }

        while (end < json.length()
                && Character.isDigit(json.charAt(end))) {

            end++;
        }

        if (end == start
                || (json.charAt(start) == '-'
                && end == start + 1)) {

            return null;
        }

        try {

            return Integer.parseInt(
                    json.substring(start, end)
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static BigDecimal getDecimal(
            String json,
            String key) {

        if (json == null || key == null) {
            return null;
        }

        String search = "\"" + key + "\":";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (start < json.length()
                && Character.isWhitespace(json.charAt(start))) {

            start++;
        }

        int end = start;

        if (end < json.length()
                && json.charAt(end) == '-') {

            end++;
        }

        while (end < json.length()
                && (Character.isDigit(json.charAt(end))
                || json.charAt(end) == '.')) {

            end++;
        }

        if (end == start) {
            return null;
        }

        try {

            return new BigDecimal(
                    json.substring(start, end)
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static <T extends Enum<T>> T getEnum(
            String json,
            String key,
            Class<T> enumClass) {

        String value = getString(json, key);

        if (value == null) {
            return null;
        }

        try {

            return Enum.valueOf(
                    enumClass,
                    value
            );

        } catch (IllegalArgumentException e) {

            return null;
        }
    }

    public static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static String toJson(
            StudentResponse student) {

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

    public static String toJsonstudents(
            List<StudentResponse> students) {

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0;
             i < students.size();
             i++) {

            json.append(
                    toJson(students.get(i))
            );

            if (i < students.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String toJson(
            CourseResponse course) {

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

    public static String toJsoncourses(
            List<CourseResponse> courses) {

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0;
             i < courses.size();
             i++) {

            json.append(
                    toJson(courses.get(i))
            );

            if (i < courses.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String toJson(
            EnrollmentResponse enrollment) {

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

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0;
             i < enrollments.size();
             i++) {

            json.append(
                    toJson(enrollments.get(i))
            );

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
                  "error": "%s"
                }
                """.formatted(
                escape(message)
        );
    }

    public static String toJson(
            PaymentResponse payment) {

        return """
                {
                  "id": %d,
                  "enrollmentId": %d,
                  "amount": %s,
                  "paymentMethod": "%s",
                  "paymentStatus": "%s",
                  "transactionReference": "%s",
                  "paymentDate": "%s"
                }
                """.formatted(
                payment.getId(),
                payment.getEnrollmentId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                escape(payment.getTransactionReference()),
                payment.getPaymentDate()
        );
    }

    public static String toJson(
            AuditLog auditLog) {

        return """
                {
                  "id": %d,
                  "action": "%s",
                  "entityType": "%s",
                  "entityId": %d,
                  "description": "%s",
                  "createdAt": "%s"
                }
                """.formatted(
                auditLog.getId(),
                escape(auditLog.getAction()),
                escape(auditLog.getEntityType()),
                auditLog.getEntityId(),
                escape(auditLog.getDescription()),
                auditLog.getCreatedAt()
        );
    }

    public static String toJsonAuditLogs(
            List<AuditLog> logs) {

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0;
             i < logs.size();
             i++) {

            json.append(
                    toJson(logs.get(i))
            );

            if (i < logs.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }


    public static String toJson(
            ErrorResponse response) {

        String detailsJson;

        if (response.getDetails() == null) {

            detailsJson = "null";

        } else {

            detailsJson =
                    "\"" +
                            escape(
                                    String.valueOf(
                                            response.getDetails()
                                    )
                            ) +
                            "\"";
        }

        return """
                {
                  "timestamp": "%s",
                  "status": %d,
                  "error": "%s",
                  "message": "%s",
                  "path": "%s",
                  "details": %s
                }
                """.formatted(
                escape(
                        String.valueOf(
                                response.getTimestamp()
                        )
                ),
                response.getStatus(),
                escape(response.getError()),
                escape(response.getMessage()),
                escape(response.getPath()),
                detailsJson
        );
    }

    public static boolean isValidJson(
            String json) {

        if (json == null
                || json.isBlank()) {

            return false;
        }

        json = json.trim();

        if (!json.startsWith("{")
                || !json.endsWith("}")) {

            return false;
        }

        int quotes = 0;

        boolean escaped = false;

        for (int i = 0;
             i < json.length();
             i++) {

            char c = json.charAt(i);

            if (c == '\\' && !escaped) {

                escaped = true;
                continue;
            }

            if (c == '"' && !escaped) {
                quotes++;
            }

            escaped = false;
        }

        return quotes % 2 == 0;
    }
}