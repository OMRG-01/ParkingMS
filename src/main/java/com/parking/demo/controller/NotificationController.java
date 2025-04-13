package com.parking.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.demo.service.NotificationService;

@RestController
@RequestMapping("/admin/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/latest")
    public List<String> getNotifications() {
        return notificationService.getNotifications(); // Return all current notifications
    }
}
