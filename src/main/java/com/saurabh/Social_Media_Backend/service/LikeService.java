package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.repo.LikeRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {
    private final LikeRepo likeRepo;
    public LikeService(LikeRepo likeRepo){
        this.likeRepo=likeRepo;
    }
    public List<TweetsResponse> findLikedTweetsByUserId(long id){
        List<TweetsResponse>list=new ArrayList<>();
        List<Tweets>tweetsList=likeRepo.findTweetsLikedByUser(id);
        for (Tweets tweets:tweetsList){

        }
        return list;
    }
}
