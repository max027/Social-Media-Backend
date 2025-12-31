package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.MentionResponse;
import com.saurabh.Social_Media_Backend.service.MentionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class MentionsController {

    private final MentionService mentionService;

    public MentionsController(MentionService mentionService) {
        this.mentionService = mentionService;
    }

    @GetMapping("/mentions")
    public ResponseEntity<List<MentionResponse>>getMentions(){
        return ResponseEntity.ok(mentionService.getMentions());
    }
}
