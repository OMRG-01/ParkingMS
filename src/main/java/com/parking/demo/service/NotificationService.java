package com.parking.demo.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private static final ConcurrentLinkedQueue<String> notifications = new ConcurrentLinkedQueue<>();

    public void addNotification(String message) {
        notifications.offer(message); // Add a new notification to the queue
    }

    public List<String> getNotifications() {
        return new ArrayList<>(notifications); // Return the list of notifications
    }

    public void clearNotifications() {
        notifications.clear(); // Optionally clear notifications after they've been seen or at regular intervals
    }
}
