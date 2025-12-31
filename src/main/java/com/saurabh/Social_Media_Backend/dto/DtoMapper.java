package com.saurabh.Social_Media_Backend.dto;

import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;

public class DtoMapper {
    private static DtoMapper dtoMapper;
    private DtoMapper(){

    }


    public static DtoMapper getDtoMapper(){
        if (dtoMapper==null){
            dtoMapper=new DtoMapper();
        }
        return dtoMapper;
    }

    public TweetsResponse toTweetsResponse(Tweets tweets){
        return new TweetsResponse(
                tweets.getTweetsId(),
                tweets.getUsers().getUserId(),
                tweets.getContent(),
                tweets.getRetweetCount(),
                tweets.getQuoteCount(),
                tweets.getReplyCount(),
                tweets.getLikeCount(),
                tweets.getBookmarkCount(),
                tweets.isRetweet(),
                tweets.getViewCount(),
                tweets.isQuote()
        );
    }
    public UserResponse toUserResponse(Users users){
        return new UserResponse(
                users.getUserId(),
                users.getUsername(),
                users.getEmail(),
                users.getBio(),
                users.getProfilePictureUrl(),
                users.getBannerUrl(),
                users.getDateOfBirth(),
                users.getFollowersCount(),
                users.getFollowingCount()
        );
    };

}
