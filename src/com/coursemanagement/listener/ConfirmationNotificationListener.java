package com.coursemanagement.listener;

import com.coursemanagement.event.EnrollmentConfirmedEvent;
import com.coursemanagement.event.EventListener;

public class ConfirmationNotificationListener
        implements EventListener {

    @Override
    public void onEnrollmentConfirmed(
            EnrollmentConfirmedEvent event) {

        System.out.println(
                "Enrollment #" +
                        event.getEnrollmentId() +
                        " has been successfully confirmed for student #" +
                        event.getStudentId()
        );
    }
}