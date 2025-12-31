package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.models.Hashtag;
import com.saurabh.Social_Media_Backend.repo.HashtagRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HashtagService {
    private final HashtagRepo hashtagRepo;
    public  HashtagService(HashtagRepo hashtagRepo){
        this.hashtagRepo=hashtagRepo;
    }
    public List<Hashtag> getAllHashtag(String hashtag){
        List<Hashtag> list=hashtagRepo.findHashtagByTag(hashtag);
        return list;
    }
}
