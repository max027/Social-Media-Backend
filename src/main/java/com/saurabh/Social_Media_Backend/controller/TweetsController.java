package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.ReplyRequest;
import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.service.TweetsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tweets")
public class TweetsController {
    private final TweetsService tweetsService;
    public TweetsController(TweetsService tweetsService){
        this.tweetsService=tweetsService;
    }
    private void validateId(long id){
        if (id < 0) {
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "invalid id: " + id);
        }
    }



    @GetMapping("/id/{id}")
    public ResponseEntity<TweetsResponse> getTweetById(@PathVariable  long id){
        validateId(id);
        TweetsResponse tweets1=tweetsService.findByTweetId(id);
        return ResponseEntity.ok(tweets1);
    }
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteTweetById(@PathVariable long id){
        validateId(id);
        tweetsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/")
    public ResponseEntity<TweetsResponse> createTweet(@RequestBody Tweets tweets){
        TweetsResponse tweets1=tweetsService.createTweet(tweets);
        return ResponseEntity.ok(tweets1);
    }

    //tweets interaction

    @PostMapping("/id/{id}/like")
    public ResponseEntity<?>likeTweet(@PathVariable long id){
        validateId(id);
        int response=tweetsService.likeTweet(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "liked",true,"LikeCount",response
                )
        );
    }

    @DeleteMapping("/id/{id}/like")
    public ResponseEntity<?>unlikeTweet(@PathVariable long id){
        validateId(id);
        tweetsService.unlikeTweet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/id/{id}/retweet")
    public ResponseEntity<?>retweet(@PathVariable long id){
        validateId(id);
        int retweetTweetCount= tweetsService.retweet(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "retweet",true,"retweetCount",retweetTweetCount
                )
        );
    }

    @DeleteMapping("/id/{id}/retweet")
    public ResponseEntity<?>UndoRetweetTweet(@PathVariable long id){
        validateId(id);
        tweetsService.undoRetweet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/id/{id}/quote")
    public ResponseEntity<?>quoteTweet(@PathVariable long id){
        validateId(id);
        int quoteTweetCount = tweetsService.quoteTweet(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "quote",true,"quoteCount", quoteTweetCount
                )
        );
    }

    @DeleteMapping("/id/{id}/quote")
    public ResponseEntity<?>deleteQuoteTweet(@PathVariable long id){
        validateId(id);
        tweetsService.undoQuote(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/id/{id}/reply")
    public ResponseEntity<?>replyTweet(@PathVariable long id, @RequestBody ReplyRequest content){
        validateId(id);
        int replyCount= tweetsService.reply(id,content);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "replyCount",replyCount
                )
        );
    }

    @DeleteMapping("/id/{id}/reply")
    public ResponseEntity<?> deleteReplyTweet(@PathVariable long id){
        validateId(id);
        tweetsService.undoReply(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/replies")
    public ResponseEntity<List<Tweets>> getReplies(@PathVariable long id){
        validateId(id);
        return ResponseEntity.ok(tweetsService.getReplies(id));
    }

    @GetMapping("/id/{id}/likes")
    public ResponseEntity<List<UserResponse>> getLikes(@PathVariable long id){
        validateId(id);
        return ResponseEntity.ok(tweetsService.getLikes(id));
    }


    @GetMapping("/id/{id}/retweets")
    public ResponseEntity<List<UserResponse>> getRetweets(@PathVariable long id){
        validateId(id);
        return ResponseEntity.ok(tweetsService.getRetweets(id));
    }

    @GetMapping("/id/{id}/quotes")
    public ResponseEntity<List<UserResponse>> getQuotes(@PathVariable long id){
        validateId(id);
        return ResponseEntity.ok(tweetsService.getQuotes(id));
    }

}
