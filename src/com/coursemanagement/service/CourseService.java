package com.coursemanagement.service;

import com.coursemanagement.dto.mapper.CourseMapper;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;

import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.enums.CourseStatus;
import com.coursemanagement.model.Course;
import com.coursemanagement.repository.CourseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CreateCourseRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank())
            throw new IllegalArgumentException("Title is required");

        if (request.getDescription() == null || request.getDescription().isBlank())
            throw new IllegalArgumentException("Description is required");

        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be greater than zero");

        if (request.getCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be greater than zero");

        Course course = new Course(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getCapacity(),
                request.getCapacity(),
                CourseStatus.Open,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        courseRepository.save(course);

        return CourseMapper.toResponse(course);
    }

    public CourseResponse findCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        return CourseMapper.toResponse(course);
    }

    public List<CourseResponse> findAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse replaceCourse(Long id, CreateCourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCapacity(request.getCapacity());
        course.setAvailableSeats(request.getCapacity());
        course.setUpdatedAt(LocalDateTime.now());

        courseRepository.save(course);

        return CourseMapper.toResponse(course);
    }

    public CourseResponse updateCourseStatus(Long id,
                                             UpdateCourseStatusRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        course.setStatus(request.getStatus());
        course.setUpdatedAt(LocalDateTime.now());

        courseRepository.save(course);

        return CourseMapper.toResponse(course);
    }

    public void deleteCourse(Long id) {

        courseRepository.deleteById(id);

    }

}