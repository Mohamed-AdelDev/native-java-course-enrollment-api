package com.coursemanagement.main;

import com.coursemanagement.handler.HttpServerManager;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        System.out.println("Course Enrollment Management System");

        HttpServerManager serverManager =
                new HttpServerManager();

        serverManager.start();

        System.out.println(
                "Application started successfully"
        );
    }
}