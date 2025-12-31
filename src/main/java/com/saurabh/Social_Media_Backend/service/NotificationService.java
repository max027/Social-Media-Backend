package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.DtoMapper;
import com.saurabh.Social_Media_Backend.dto.NotificationResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Notification;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.NotificatioinsRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {
    private final NotificatioinsRepo notificatioinRepo;
    private final getCurrentUserService getCurrentUserService;
    private final DtoMapper mapper=DtoMapper.getDtoMapper();

    public Notification checkById(long id){
        return notificatioinRepo.findById(id).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "notification not found")
        );
    }

    public NotificationService(NotificatioinsRepo notificatioinRepo, getCurrentUserService getCurrentUserService) {
        this.notificatioinRepo = notificatioinRepo;
        this.getCurrentUserService = getCurrentUserService;
    }

    public List<NotificationResponse>getAllNotification(){
        Users users=getCurrentUserService.getCurrentUser();
        List<Notification> list=notificatioinRepo.findNotificationByUsers(users);
        return list.stream().map(mapper::toNotificationResponse).toList();
    }

    public void deleteNotification(long id){
        Notification notification=checkById(id);
        notificatioinRepo.delete(notification);
    }
    public void readNotification(long id){
        Notification notification=checkById(id);
        notification.setNotificationId(id);
        notification.setRead(true);
        notificatioinRepo.save(notification);
    }
    public void markAllNotification(){
        Users users=getCurrentUserService.getCurrentUser();
        List<Notification>notifications=notificatioinRepo.findNotificationByUsers(users);
        for(Notification n:notifications){
            n.setNotificationId(n.getNotificationId());
            n.setRead(true);
            notificatioinRepo.save(n);
        }
    }
}
