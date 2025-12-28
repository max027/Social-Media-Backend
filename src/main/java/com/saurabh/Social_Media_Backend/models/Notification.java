package com.saurabh.Social_Media_Backend.models;

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
    private enum NotificationType{
        LIKE,
        FOLLOW,
        RETWEET,
        QUOTE,
        REPLY,
        MENTION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private long notificationId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users userId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "actor_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users Actor_id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "tweet_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tweets tweetId;

    @Column(name = "is_read")
    private boolean isRead=false;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

}
