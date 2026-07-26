package com.coursemanagement.main;

import com.coursemanagement.handler.HttpServerManager;

import java.io.IOException;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.model.*;
import com.coursemanagement.model.enums.*;
import com.coursemanagement.repository.*;
import com.coursemanagement.service.CourseService;
import com.coursemanagement.service.StudentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Application {

    public static void main(String[] args) throws IOException {

        System.out.println("Course Enrollment Management System");

        HttpServerManager serverManager = new HttpServerManager();
        serverManager.start();

        System.out.println("Application started successfully");
    }}