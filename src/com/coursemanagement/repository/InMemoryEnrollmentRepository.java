package com.coursemanagement.repository;

import com.coursemanagement.model.Enrollment;

import java.util.HashMap;
import java.util.*;
import java.util.Optional;

public class InMemoryEnrollmentRepository implements EnrollmentRepository{
    private final Map<Long , Enrollment> enrollments = new HashMap<>();

    private long nextId = 1l ;

    @Override
    public Enrollment save(Enrollment enrollment) {

        if (enrollment.getId() == null) {
            enrollment.setId(nextId++);
        }

        enrollments.put(enrollment.getId(), enrollment);

        return enrollment;
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    @Override
    public List<Enrollment> findByStudentId(Long studentId) {

        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getStudentId().equals(studentId)) {
                result.add(enrollment);
            }

        }

        return result;
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getStudentId().equals(studentId)
                    && enrollment.getCourseId().equals(courseId)) {

                return true;
            }

        }

        return false;
    }

    @Override
    public void deleteById(Long id) {
        enrollments.remove(id);
    }
}
