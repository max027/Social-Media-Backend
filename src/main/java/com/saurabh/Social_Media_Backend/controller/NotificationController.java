package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.NotificationResponse;
import com.saurabh.Social_Media_Backend.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(){
       return ResponseEntity.ok(notificationService.getAllNotification());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable long id){
       notificationService.deleteNotification(id);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}/read")
    public ResponseEntity<?> readNotification(@PathVariable long id){
        notificationService.readNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/mark-all")
    public ResponseEntity<?> markAllNotification(){
        notificationService.markAllNotification();
        return ResponseEntity.noContent().build();
    }
}
