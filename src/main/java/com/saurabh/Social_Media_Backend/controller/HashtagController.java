package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.service.HashtagService;
import org.apache.coyote.Response;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {
    private final HashtagService hashtagService;
    public HashtagController(HashtagService hashtagService){
        this.hashtagService=hashtagService;
    }
    @GetMapping("/{hashtag}")
    public ResponseEntity<?> getHashtags(@PathVariable String hashtag){
        return  ResponseEntity.ok(hashtagService.getAllHashtag(hashtag));
    }

}
