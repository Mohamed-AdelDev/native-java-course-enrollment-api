package com.coursemanagement.event;

public interface EventListener {

    void onEnrollmentConfirmed(
            EnrollmentConfirmedEvent event
    );
}