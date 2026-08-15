package com.coursemanagement.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventPublisher {

    private static volatile EventPublisher instance;

    private final List<EventListener> listeners =
            new CopyOnWriteArrayList<>();

    private EventPublisher() {
    }

    public static EventPublisher getInstance() {

        if (instance == null) {

            synchronized (EventPublisher.class) {

                if (instance == null) {
                    instance = new EventPublisher();
                }
            }
        }

        return instance;
    }

    public void subscribe(EventListener listener) {

        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void publish(
            EnrollmentConfirmedEvent event) {

        for (EventListener listener : listeners) {

            listener.onEnrollmentConfirmed(event);
        }
    }
}