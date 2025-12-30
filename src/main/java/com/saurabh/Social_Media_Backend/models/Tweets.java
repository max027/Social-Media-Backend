package com.saurabh.Social_Media_Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tweets")
public class Tweets extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweets_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Long tweetsId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;

    @Column(name = "content")
    private String content;

    //spring.jpa.hibernate.ddl-auto=none
    @OneToMany
    @JoinColumn(name = "media_url")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MediaUrls>mediaUrls=new ArrayList<>();

    @Column(name = "retweet_count")
    private int retweetCount=0;

    @Column(name = "quote_count")
    private int quoteCount=0;

    @Column(name = "reply_count")
    private int replyCount=0;


    @Column(name = "like_count")
    private int likeCount=0;


    @Column(name = "bookmark_count")
    private int bookmarkCount=0;

    @Column(name = "is_retweet")
    private boolean isRetweet=false;


    @Column(name = "view_count")
    private int viewCount=0;

    @Column(name = "is_quote_tweet")
    private boolean isQuote=false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quoted_tweet_id")
    private Tweets quoteTweetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_tweet_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tweets originalTweetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_tweet_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tweets replyToTweet;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users replyToUserId;

}
