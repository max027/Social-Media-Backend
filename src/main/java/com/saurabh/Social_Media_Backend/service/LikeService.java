package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.DtoMapper;
import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.repo.LikeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private final LikeRepo likeRepo;
    private final DtoMapper dtoMapper=DtoMapper.getDtoMapper();
    public LikeService(LikeRepo likeRepo){
        this.likeRepo=likeRepo;
    }
    public List<TweetsResponse> findLikedTweetsByUserId(long id){
        List<Tweets>tweetsList=likeRepo.findTweetsLikedByUser(id);
        return tweetsList.stream().map(dtoMapper::toTweetsResponse).toList();
    }
}
