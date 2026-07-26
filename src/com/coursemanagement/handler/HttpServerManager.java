package com.coursemanagement.handler;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {

    private HttpServer server;

    public void start() throws IOException {

        server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/students", new StudentHandler());
        server.createContext("/api/courses", new CourseHandler());
        server.createContext("/api/auth", new AuthHandler());
        server.createContext("/api/enrollments", new EnrollmentHandler());
        server.createContext("/api/payments", new PaymentHandler());
        server.createContext("/api/health", new HealthHandler());

        server.setExecutor(null);

        server.start();

        System.out.println("HTTP Server started on http://localhost:8080");
    }
}