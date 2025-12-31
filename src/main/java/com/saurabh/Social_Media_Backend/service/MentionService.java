package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.MentionResponse;
import com.saurabh.Social_Media_Backend.models.Mentions;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.MentionsRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentionService {

    private final MentionsRepo mentionsRepo;
    private final getCurrentUserService getCurrentUserService;

    public MentionService(MentionsRepo mentionsRepo, getCurrentUserService getCurrentUserService) {
        this.mentionsRepo = mentionsRepo;
        this.getCurrentUserService = getCurrentUserService;
    }

    public List<MentionResponse> getMentions(){
        Users users=getCurrentUserService.getCurrentUser();
       List<Mentions>list=mentionsRepo.findMentionsByUsers(users);

       return list.stream().map((mentions)->{
          return new MentionResponse(
                  mentions.getUsers().getUserId(),
                  mentions.getTweets().getTweetsId());
       }).toList();
    }
}
