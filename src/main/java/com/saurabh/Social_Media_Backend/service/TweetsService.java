package com.saurabh.Social_Media_Backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.saurabh.Social_Media_Backend.dto.TweetsMapper;
import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.TweetsRepo;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import com.saurabh.Social_Media_Backend.utils.SecurityUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TweetsService {
   private final TweetsRepo tweetsRepo;
   private final UserRepo userRepo;

   private final TweetsMapper tweetsMapper= Mappers.getMapper(TweetsMapper.class);


   private Tweets checkIfExist(long id){
      Optional<Tweets>tweets=tweetsRepo.findById(id);
      if (tweets.isEmpty()){
          throw new AppException(HttpStatus.NOT_FOUND.value(), "Tweet with id:"+id+" not found");
      }
      return tweets.get();
   }

   private TweetsResponse createTweetResponse(Tweets tweets){
       return  tweetsMapper.toResponse(tweets);
   }

   public TweetsService(TweetsRepo tweetsRepo,UserRepo userRepo){
       this.tweetsRepo=tweetsRepo;
       this.userRepo=userRepo;
   }
   public List<TweetsResponse>findByUserId(long id){
       List<TweetsResponse>list=new ArrayList<>();
       List<Tweets>tweetsList=tweetsRepo.findByUsersUserId(id);
       for (Tweets tweets:tweetsList){
           TweetsResponse tweetsResponse=createTweetResponse(tweets);
           list.add(tweetsResponse);
       }
       return list;
   }
   public TweetsResponse findByTweetId(long id){
       Tweets tweets=checkIfExist(id);
       return createTweetResponse(tweets);
   }

   public void deleteById(long id){
       Tweets tweets=checkIfExist(id);
       Users users=userRepo.findByEmail(SecurityUtils.getCurrentUser()).orElseThrow();

       deleteByOwnersId(users.getUserId(),id);
   }

   private void deleteByOwnersId(long userId,long tweetId){
       Tweets tweets=tweetsRepo.findById(tweetId).orElseThrow();
       if (!tweets.getUsers().getUserId().equals(userId)){
           throw new AppException(HttpStatus.FORBIDDEN.value(), "Forbidden");
       }

       tweetsRepo.deleteById(tweetId);
   }

   @Transactional
   public TweetsResponse createTweet(Tweets tweets){
       String email=SecurityUtils.getCurrentUser();
       Users users=userRepo.findByEmail(email).orElseThrow();
       tweets.setUsers(users);
       Tweets tweets1=tweetsRepo.save(tweets);
       return createTweetResponse(tweets1);
   }
}
