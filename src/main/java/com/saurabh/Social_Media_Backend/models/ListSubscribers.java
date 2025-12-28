package com.saurabh.Social_Media_Backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "list_subscribers",uniqueConstraints = {
        @UniqueConstraint(name = "unique_subscriber ",columnNames = {"user_id","list_id"})
})
public class ListSubscribers extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_subscriber_id")
    private long listSubscriberId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users userId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "list_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List listId;


    @CreationTimestamp
    @Column(name = "subscribed_at",updatable = false,nullable = false)
    private LocalDateTime subscribed_at;
}
