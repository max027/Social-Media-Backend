package com.saurabh.Social_Media_Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetsResponse {
    private long id;
    private long userId;
    private String content;
    private int retweetCount=0;
    private int quoteCount=0;
    private int replyCount=0;
    private int likeCount=0;
    private int bookmarkCount=0;
    private boolean isRetweet=false;
    private int viewCount=0;
    private boolean isQuote=false;

}
