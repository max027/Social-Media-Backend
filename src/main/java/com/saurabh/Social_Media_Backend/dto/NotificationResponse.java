package com.saurabh.Social_Media_Backend.dto;

import com.saurabh.Social_Media_Backend.models.Notification;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class NotificationResponse {
private long userId;
private long ActorId;
private long tweetId;
private boolean isRead;
private NotificationType notificationType;
}
