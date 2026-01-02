package com.saurabh.Social_Media_Backend.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
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
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Users users;

    @Column(name = "content")
    private String content;

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

    //for retweets
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
