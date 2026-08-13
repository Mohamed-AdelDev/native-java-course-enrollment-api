package com.coursemanagement.service;

import com.coursemanagement.dto.mapper.CourseMapper;
import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.model.Course;
import com.coursemanagement.model.enums.CourseStatus;
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

        validateCourseRequest(request);

        Course course = new Course(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getCapacity(),
                request.getCapacity(),
                request.getStatus(),
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



    public CourseResponse replaceCourse(
            Long id,
            CreateCourseRequest request
    ) {


        validateCourseRequest(request);

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCapacity(request.getCapacity());


        course.setAvailableSeats(request.getCapacity());

        course.setStatus(request.getStatus());
        course.setUpdatedAt(LocalDateTime.now());

        courseRepository.save(course);

        return CourseMapper.toResponse(course);
    }


    public CourseResponse updateCourseStatus(
            Long id,
            UpdateCourseStatusRequest request
    ) {

        if (request == null || request.getStatus() == null) {
            throw new IllegalArgumentException("Status is required");
        }

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));


        course.setStatus(request.getStatus());

        course.setUpdatedAt(LocalDateTime.now());

        courseRepository.save(course);

        return CourseMapper.toResponse(course);
    }


    public void deleteCourse(Long id) {

        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found");
        }

        courseRepository.deleteById(id);
    }


    public List<CourseResponse> findCoursesByStatus(
            CourseStatus status
    ) {

        return courseRepository.findAll()
                .stream()
                .filter(course ->
                        course.getStatus() == status)
                .map(CourseMapper::toResponse)
                .collect(Collectors.toList());
    }


    public List<CourseResponse> findCourses(
            CourseStatus status,
            String title,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sort
    ) {


        if (minPrice != null
                && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "minPrice cannot be greater than maxPrice"
            );
        }

        if (sort != null
                && !sort.equalsIgnoreCase("price,asc")
                && !sort.equalsIgnoreCase("price,desc")) {

            throw new IllegalArgumentException(
                    "Invalid sort parameter"
            );
        }

        return courseRepository.findAll()
                .stream()

                .filter(course ->
                        status == null
                                || course.getStatus() == status)

                .filter(course ->
                        title == null
                                || course.getTitle()
                                .toLowerCase()
                                .contains(title.toLowerCase()))

                .filter(course ->
                        minPrice == null
                                || course.getPrice()
                                .compareTo(minPrice) >= 0)


                .filter(course ->
                        maxPrice == null
                                || course.getPrice()
                                .compareTo(maxPrice) <= 0)

                .sorted((c1, c2) -> {

                    if (sort == null) {
                        return 0;
                    }

                    if (sort.equalsIgnoreCase("price,asc")) {
                        return c1.getPrice()
                                .compareTo(c2.getPrice());
                    }

                    return c2.getPrice()
                            .compareTo(c1.getPrice());
                })

                .map(CourseMapper::toResponse)

                .collect(Collectors.toList());
    }

    private void validateCourseRequest(
            CreateCourseRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Course data is required"
            );
        }

        if (request.getTitle() == null
                || request.getTitle().isBlank()) {

            throw new IllegalArgumentException(
                    "Title is required"
            );
        }

        if (request.getDescription() == null
                || request.getDescription().isBlank()) {

            throw new IllegalArgumentException(
                    "Description is required"
            );
        }

        if (request.getPrice() == null
                || request.getPrice()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Price must be greater than zero"
            );
        }

        if (request.getCapacity() <= 0) {

            throw new IllegalArgumentException(
                    "Capacity must be greater than zero"
            );
        }

        if (request.getStatus() == null) {

            throw new IllegalArgumentException(
                    "Status is required"
            );
        }
    }
}