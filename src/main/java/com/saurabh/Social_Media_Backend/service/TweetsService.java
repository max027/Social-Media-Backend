package com.saurabh.Social_Media_Backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.saurabh.Social_Media_Backend.dto.*;
import com.saurabh.Social_Media_Backend.models.*;
import com.saurabh.Social_Media_Backend.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.utils.SecurityUtils;

@Service
@Transactional
public class TweetsService {
    private final TweetsRepo tweetsRepo;
    private final UserRepo userRepo;
    private final LikeRepo likeRepo;
    private final HashtagRepo hashtagRepo;
    private final getCurrentUserService getCurrentUserService;
    private final DtoMapper mapper=DtoMapper.getDtoMapper();
    private final MentionsRepo mentionsRepo;
    private final NotificatioinsRepo notificatioinsRepo;

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

    public TweetsService(TweetsRepo tweetsRepo, UserRepo userRepo, LikeRepo likeRepo, HashtagRepo hashtagRepo, getCurrentUserService getCurrentUserService, MentionsRepo mentionsRepo, NotificatioinsRepo notificatioinsRepo){
        this.tweetsRepo=tweetsRepo;
        this.userRepo=userRepo;
        this.likeRepo=likeRepo;
        this.hashtagRepo=hashtagRepo;
        this.getCurrentUserService=getCurrentUserService;
        this.mentionsRepo = mentionsRepo;
        this.notificatioinsRepo = notificatioinsRepo;
    }
    public List<TweetsResponse>findByUserId(long id){
        List<Tweets>tweetsList=tweetsRepo.findByUsersUserId(id);
        return tweetsList.stream().map(mapper::toTweetsResponse).toList();
    }
    public TweetsResponse findByTweetId(long id){
        Tweets tweets=checkIfExist(id);
        return createTweetResponse(tweets);
    }

    private void sendNofication(NotificationType type,Users users,Users actors,Tweets tweets){
        Notification notification=new Notification();
        notification.setUsers(users);
        notification.setActors(actors);
        notification.setTweets(tweets);
        notificatioinsRepo.save(notification);
    }
    public void deleteById(long id){
        Users users=getCurrentUserService.getCurrentUser();
        checkIfExist(id);
        Tweets tweets=tweetsRepo.findById(id).orElseThrow();
        if (!tweets.getUsers().getUserId().equals(users.getUserId())){
            throw new AppException(HttpStatus.FORBIDDEN.value(), "Forbidden");
        }
        tweetsRepo.deleteById(id);
    }

    public TweetsResponse createTweet(Tweets tweets){
        String email=SecurityUtils.getCurrentUser();
        Users users=userRepo.findByEmail(email).orElseThrow();
        tweets.setUsers(users);
        //check hashtag if contain
        Set<String>hashtag=extractHashtag(tweets.getContent());

        //save tweet first
        Tweets tweets1=tweetsRepo.save(tweets);

        //save hashtag
        if (!hashtag.isEmpty()){
            for(String tag:hashtag){
                Hashtag s=hashtagRepo.findByTag(tag).orElseGet(
                        ()->{
                            Hashtag newTag=new Hashtag();
                            newTag.setTag(tag);
                            newTag.setTweetCount(0);
                            return newTag;
                        }
                );
                s.setTweetCount(s.getTweetCount()+1);
                hashtagRepo.save(s);
            }

        }

        Set<String> mentions=extractMentions(tweets.getContent());
        //save mentions
        if (!mentions.isEmpty()){
            for (String user:mentions){
               Users users1=userRepo.findByUsername(user);
               Mentions mentions1=new Mentions();
               mentions1.setUsers(users1);
               mentions1.setTweets(tweets);

               mentionsRepo.save(mentions1);
            }
        }
        return createTweetResponse(tweets1);
    }
    private Set<String> extractMentions(String content){
        Pattern mentionsPattern=Pattern.compile("(?<![a-zA-Z0-9_])@([a-zA-Z0-9_]{1,15})");
        Matcher matcher=mentionsPattern.matcher(content);

        Set<String>mentions=new HashSet<>();
        while (matcher.find()){
            mentions.add(matcher.group(1).toLowerCase());
        }
        return mentions;
    }

    private Set<String>extractHashtag(String content) {
        Pattern hashtagPattern=Pattern.compile("@([a-zA-Z0-9_]+)");
        Matcher matcher=hashtagPattern.matcher(content);
        Set<String>hashtag=new HashSet<>();
        while (matcher.find()) {
            hashtag.add(matcher.group(1).toLowerCase());
        }
        return hashtag;
    }

    public int likeTweet(long tweetsId) {
        Users users =getCurrentUserService.getCurrentUser();
        Tweets tweets = checkIfExist(tweetsId);
        // like
        Likes likes = new Likes();
        likes.setTweets(tweets);
        likes.setUsers(users);
        likeRepo.save(likes);

        // inc like count
        tweets.setLikeCount(tweets.getLikeCount() + 1);

        sendNofication(NotificationType.LIKE,users,tweets.getUsers(),tweets);

        return tweets.getLikeCount();
    }

    public void unlikeTweet(long tweetsId) {
        // check user
        Users users =getCurrentUserService.getCurrentUser();

        // check tweet
        Tweets tweets = checkIfExist(tweetsId);

        // check if user liked tweet
        Likes like = likeRepo.findLikesByTweetsAndUsers(tweets, users).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND.value(), "use did not like"));

        likeRepo.deleteById(like.getLikeId());

        // dec like count
        tweets.setLikeCount(tweets.getLikeCount() - 1);
    }

    public int retweet(long id) {
        Tweets tweets = checkIfExist(id);
        Users users = getCurrentUserService.getCurrentUser();
        Tweets retweet = new Tweets();

        // inc retweet count of original
        tweets.setRetweetCount(tweets.getRetweetCount() + 1);

        // set retweet content and other
        retweet.setRetweet(true);
        retweet.setOriginalTweetId(tweets);
        retweet.setUsers(users);

        // save
        tweetsRepo.save(retweet);
        sendNofication(NotificationType.RETWEET,users,tweets.getUsers(),tweets);
        return tweets.getRetweetCount();
    }

    public void undoRetweet(long id) {
        Tweets originalTweet = checkIfExist(id);
        Users users = getCurrentUserService.getCurrentUser();
        Tweets retweet = tweetsRepo.findTweetsByUsersAndOriginalTweetId(users, originalTweet).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND.value(),
                        "Tweet not fount id:" + originalTweet.getTweetsId() + " useID:" + users.getUserId()));

        originalTweet.setRetweetCount(originalTweet.getRetweetCount() - 1);

        tweetsRepo.deleteById(retweet.getTweetsId());

    }

    public int quoteTweet(long id) {
        Tweets tweets = checkIfExist(id);
        Users users =getCurrentUserService.getCurrentUser();

        Tweets quote = new Tweets();

        // inc quote count of original
        tweets.setQuoteCount(tweets.getQuoteCount() + 1);

        // set quote content and other
        quote.setQuote(true);
        quote.setQuoteTweetId(tweets);
        quote.setUsers(users);

        // save
        tweetsRepo.save(quote);

        sendNofication(NotificationType.QUOTE,users,tweets.getUsers(),tweets);
        return tweets.getQuoteCount();
    }

    public void undoQuote(long id) {
        Tweets originalTweet = checkIfExist(id);
        Users users =getCurrentUserService.getCurrentUser();

        Tweets quote = tweetsRepo.findTweetsByUsersAndQuoteTweetId(users, originalTweet).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND.value(),
                        "Quote Tweet not fount id:" + originalTweet.getTweetsId() + " useID:" + users.getUserId()));

        // dec quote count original
        originalTweet.setQuoteCount(originalTweet.getQuoteCount() - 1);

        // delet quote tweet
        tweetsRepo.deleteById(quote.getTweetsId());
    }

    public int reply(long id, ReplyRequest ReplyContent) {
        Tweets tweets = checkIfExist(id);
        Users users = getCurrentUserService.getCurrentUser();

        Tweets reply = new Tweets();

        // inc reply count
        tweets.setReplyCount(tweets.getReplyCount() + 1);

        // set reply users
        reply.setUsers(users);

        // set tweets
        reply.setReplyToTweet(tweets);

        // set content
        reply.setContent(ReplyContent.content());

        // set users
        reply.setReplyToUserId(tweets.getUsers());

        tweetsRepo.save(reply);

        sendNofication(NotificationType.REPLY,users,tweets.getUsers(),tweets);

        return tweets.getReplyCount();
    }

    public void undoReply(long id) {
        Tweets originalTweet = checkIfExist(id);
        Users users = getCurrentUserService.getCurrentUser();
        Tweets reply = tweetsRepo.findTweetsByUsersAndReplyToTweet(users, originalTweet).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND.value(),
                        "Quote Tweet not fount id:" + originalTweet.getTweetsId() + " useID:" + users.getUserId()));

        // dec quote count original
        originalTweet.setReplyCount(originalTweet.getReplyCount() - 1);

        // delet quote tweet
        tweetsRepo.deleteById(reply.getTweetsId());
    }

    public List<Tweets> getReplies(long id) {
        Tweets originalTweet = checkIfExist(id);
        return tweetsRepo.findTweetsByReplyToTweet(originalTweet);
    }

    public List<UserResponse> getLikes(long id) {
        Tweets originalTweet = checkIfExist(id);
        List<Users> list = likeRepo.findUsersWhoLikedTweet(originalTweet.getTweetsId());
        return list.stream().map(mapper::toUserResponse).toList();
    }

    public List<UserResponse> getRetweets(long id) {
        Tweets originalTweet = checkIfExist(id);
        List<Users> users = userRepo.findUsersWhoRetweeted(originalTweet);
        return users.stream().map(mapper::toUserResponse).toList();
    }

    public List<UserResponse> getQuotes(long id) {
        Tweets originalTweet = checkIfExist(id);
        List<Users> users = userRepo.findUsersWhoQuoted(originalTweet);
        return users.stream().map(mapper::toUserResponse).toList();
    }
}
