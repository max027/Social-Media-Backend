package com.saurabh.Social_Media_Backend.dto;

public record TweetsResponse(
    long id,
    long userId,
    String content,
    int retweetCount,
    int quoteCount,
     int replyCount,
     int likeCount,
     int bookmarkCount,
     boolean isRetweet,
     int viewCount,
     boolean isQuote
){}
