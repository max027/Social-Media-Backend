package com.saurabh.Social_Media_Backend.dto;

import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Notification;
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
    public ListsResponse toListsResponse(Lists lists){
        return new ListsResponse(
                lists.getListId(),
                lists.getUserId(),
                lists.getListName(),
                lists.getDescription(),
                lists.isPrivate(),
                lists.getMembersCount(),
                lists.getSubscriberCount()
        );
    }
    public NotificationResponse toNotificationResponse(Notification notification){
        NotificationResponse response=new NotificationResponse();
        response.setRead(notification.isRead());
        response.setActorId(notification.getActors().getUserId());
        response.setTweetId(notification.getTweets().getTweetsId());
        response.setUserId(notification.getUsers().getUserId());
        response.setNotificationType(notification.getNotificationType());
        return response;
    }

}
