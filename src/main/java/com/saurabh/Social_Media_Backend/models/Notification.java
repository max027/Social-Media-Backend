package com.saurabh.Social_Media_Backend.models;

import com.saurabh.Social_Media_Backend.dto.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private long notificationId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users users;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "actor_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users Actors;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "tweet_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tweets tweets;

    @Column(name = "is_read")
    private boolean isRead=false;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

}
