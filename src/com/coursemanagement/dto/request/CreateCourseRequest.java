package com.coursemanagement.dto.request;


import java.math.BigDecimal;


public class CreateCourseRequest {


    private String title;


    private String description;


    private BigDecimal price;


    private int capacity;

    public CreateCourseRequest(String title, String description, BigDecimal price, int capacity) {

        this.title=title;
        this.description=description;
        this.price=price;
        this.capacity=capacity;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}