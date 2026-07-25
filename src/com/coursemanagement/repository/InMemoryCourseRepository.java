package com.coursemanagement.repository;

import com.coursemanagement.model.Course;

import java.util.*;
import java.util.Optional;

public class InMemoryCourseRepository implements CourseRepository{
    private final Map<Long, Course> courses = new HashMap<>();

    private Long nextId = 1L;

    @Override
    public Course save(Course course) {

        if (course.getId() == null) {
            course.setId(nextId++);
        }

        courses.put(course.getId(), course);

        return course;
    }

    @Override
    public Optional<Course> findById(Long id) {
        return Optional.ofNullable(courses.get(id));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public void deleteById(Long id) {
        courses.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return courses.containsKey(id);
    }
}
