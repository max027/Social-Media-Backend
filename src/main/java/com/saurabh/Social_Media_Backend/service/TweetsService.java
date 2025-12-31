package com.saurabh.Social_Media_Backend.service;

import java.util.List;
import java.util.Optional;

import com.saurabh.Social_Media_Backend.dto.*;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.LikeRepo;
import com.saurabh.Social_Media_Backend.repo.TweetsRepo;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import com.saurabh.Social_Media_Backend.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TweetsService {
   private final TweetsRepo tweetsRepo;
   private final UserRepo userRepo;
   private final LikeRepo likeRepo;

    private final DtoMapper mapper=DtoMapper.getDtoMapper();

    private Tweets checkIfExist(long id){
        Optional<Tweets>tweets=tweetsRepo.findById(id);
        if (tweets.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "Tweet with id:"+id+" not found");
        }
        return tweets.get();
    }

    private TweetsResponse createTweetResponse(Tweets tweets){
        return  mapper.toTweetsResponse(tweets);
    }

    public TweetsService(TweetsRepo tweetsRepo, UserRepo userRepo, LikeRepo likeRepo){
        this.tweetsRepo=tweetsRepo;
        this.userRepo=userRepo;
        this.likeRepo=likeRepo;
    }
    public List<TweetsResponse>findByUserId(long id){
        List<Tweets>tweetsList=tweetsRepo.findByUsersUserId(id);
        return tweetsList.stream().map(mapper::toTweetsResponse).toList();
    }
    public TweetsResponse findByTweetId(long id){
        Tweets tweets=checkIfExist(id);
        return createTweetResponse(tweets);
    }

    public void deleteById(long id){
        Users users=checkUser();
        Tweets tweets=checkIfExist(id);

        if (checkOwnerShip(users.getUserId(),id)){
            tweetsRepo.deleteById(id);
        }
    }

    //check ownership of tweet and likes
    private boolean checkOwnerShip(long userId,long Id){
        Tweets tweets=tweetsRepo.findById(Id).orElseThrow();
        if (!tweets.getUsers().getUserId().equals(userId)){
            throw new AppException(HttpStatus.FORBIDDEN.value(), "Forbidden");
        }
        return true;
    }

    //check loggedIn users
    private Users checkUser(){
        return userRepo.findByEmail(SecurityUtils.getCurrentUser()).orElseThrow(
                ()-> new UsernameNotFoundException("user not found")
        );
    }

    public TweetsResponse createTweet(Tweets tweets){
        String email=SecurityUtils.getCurrentUser();
        Users users=userRepo.findByEmail(email).orElseThrow();
        tweets.setUsers(users);
        Tweets tweets1=tweetsRepo.save(tweets);
        return createTweetResponse(tweets1);
    }

    public int likeTweet(long tweetsId){
        Users users=checkUser();
        Tweets tweets=checkIfExist(tweetsId);
        //like
        Likes likes=new Likes();
        likes.setTweets(tweets);
        likes.setUsers(users);
        likeRepo.save(likes);

        //inc like count
        tweets.setLikeCount(tweets.getLikeCount()+1);

        return tweets.getLikeCount();
    }
    public void unlikeTweet(long tweetsId){
        // check user
        Users users=checkUser();

        //check tweet
        Tweets tweets=checkIfExist(tweetsId);

        //check if user liked tweet
        Likes like=likeRepo.findLikesByTweetsAndUsers(tweets,users).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(),"use did not like")
        );

        likeRepo.deleteById(like.getLikeId());

        //dec like count
        tweets.setLikeCount(tweets.getLikeCount()-1);
    }

    public int retweet(long id){
        Tweets tweets=checkIfExist(id);
        Users users=checkUser();
        Tweets retweet=new Tweets();

        //inc retweet count of original
        tweets.setRetweetCount(tweets.getRetweetCount()+1);

        //set retweet content and other
        retweet.setRetweet(true);
        retweet.setOriginalTweetId(tweets);
        retweet.setUsers(users);

        //save
        tweetsRepo.save(retweet);

        return tweets.getRetweetCount();
    }

    public void undoRetweet(long id){
        Tweets originalTweet=checkIfExist(id);
        Users users=checkUser();

        Tweets retweet=tweetsRepo.findTweetsByUsersAndOriginalTweetId(users,originalTweet).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "Tweet not fount id:"+originalTweet.getTweetsId()+" useID:"+users.getUserId())
        );

        originalTweet.setRetweetCount(originalTweet.getRetweetCount()-1);

        tweetsRepo.deleteById(retweet.getTweetsId());

    }

    public int quoteTweet(long id){
        Tweets tweets=checkIfExist(id);
        Users users=checkUser();

        Tweets quote=new Tweets();

        //inc quote count of original
        tweets.setQuoteCount(tweets.getQuoteCount()+1);

        //set quote content and other
        quote.setQuote(true);
        quote.setQuoteTweetId(tweets);
        quote.setUsers(users);

        //save
        tweetsRepo.save(quote);

        return tweets.getQuoteCount();
    }


    public void undoQuote(long id){
        Tweets originalTweet=checkIfExist(id);
        Users users=checkUser();

        Tweets quote=tweetsRepo.findTweetsByUsersAndQuoteTweetId(users,originalTweet).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "Quote Tweet not fount id:"+originalTweet.getTweetsId()+" useID:"+users.getUserId())
        );

        //dec quote count original
        originalTweet.setQuoteCount(originalTweet.getQuoteCount()-1);

        //delet quote tweet
        tweetsRepo.deleteById(quote.getTweetsId());
    }

    public int reply(long id, ReplyRequest ReplyContent){
        Tweets tweets=checkIfExist(id);
        Users users=checkUser();

        Tweets reply=new Tweets();

        //inc reply count
        tweets.setReplyCount(tweets.getReplyCount()+1);


        //set reply users
        reply.setUsers(users);

        //set tweets
        reply.setReplyToTweet(tweets);

        // set content
        reply.setContent(ReplyContent.content());

        //set users
        reply.setReplyToUserId(tweets.getUsers());

        tweetsRepo.save(reply);

        return tweets.getReplyCount();
    }

    public void undoReply(long id){
        Tweets originalTweet=checkIfExist(id);
        Users users=checkUser();

        Tweets reply=tweetsRepo.findTweetsByUsersAndReplyToTweet(users,originalTweet).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "Quote Tweet not fount id:"+originalTweet.getTweetsId()+" useID:"+users.getUserId())
        );

        //dec quote count original
        originalTweet.setReplyCount(originalTweet.getReplyCount()-1);

        //delet quote tweet
        tweetsRepo.deleteById(reply.getTweetsId());
    }

    public List<Tweets>getReplies(long id){
        Tweets originalTweet=checkIfExist(id);
        return tweetsRepo.findTweetsByReplyToTweet(originalTweet);
    }

    public List<UserResponse>getLikes(long id){
        Tweets originalTweet=checkIfExist(id);
        List<Users>list=likeRepo.findUsersWhoLikedTweet(originalTweet.getTweetsId());
        return list.stream().map(mapper::toUserResponse).toList();
    }
    public List<UserResponse>getRetweets(long id){
        Tweets originalTweet=checkIfExist(id);
        List<Users>users=userRepo.findUsersWhoRetweeted(originalTweet);
        return users.stream().map(mapper::toUserResponse).toList();
    }
    public List<UserResponse> getQuotes(long id){
        Tweets originalTweet=checkIfExist(id);
        List<Users>users=userRepo.findUsersWhoQuoted(originalTweet);
        return users.stream().map(mapper::toUserResponse).toList();
    }
}
