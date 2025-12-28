package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.TweetsMapper;
import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.service.TweetsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
