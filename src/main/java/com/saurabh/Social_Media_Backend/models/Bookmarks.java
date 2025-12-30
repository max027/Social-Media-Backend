package com.saurabh.Social_Media_Backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "bookmarks",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_bookmark",columnNames = {
                        "user_id","tweets_id"
                })
        }
)
public class Bookmarks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private long BookmarkId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "tweets_id",nullable = false)
    private Tweets tweetsId;

    @CreatedDate
    @Column(updatable = false,name = "created_at")
    private LocalDateTime createdAt;
}
