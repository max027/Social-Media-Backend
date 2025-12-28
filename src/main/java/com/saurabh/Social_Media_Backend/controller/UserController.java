package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.service.LikeService;
import com.saurabh.Social_Media_Backend.service.TweetsService;
import com.saurabh.Social_Media_Backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TweetsService tweetsService;
    private final LikeService likeService;

    public UserController(UserService service,TweetsService tweetsService,LikeService likeService){
        this.userService =service;
        this.tweetsService=tweetsService;
        this.likeService=likeService;
    }
    private void validateId(long id){
        if (id < 0) {
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "invalid id: " + id);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable  String username){
        UserResponse users= userService.findByUsername(username);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable long id) throws ResponseStatusException {
        validateId(id);
        UserResponse users = userService.findById(id);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateById(@PathVariable long id,@RequestBody Users users ){
        validateId(id);
        UserResponse updated_user= userService.updateById(id,users);
        return ResponseEntity.ok(updated_user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        validateId(id);
       userService.deleteById(id);
       return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/tweets")
    public ResponseEntity<List<TweetsResponse>> getTweetsByUserId(@PathVariable  long id){
        validateId(id);
        return ResponseEntity.ok(tweetsService.findByUserId(id));
    }

    @GetMapping("/id/{id}/likes")
    public ResponseEntity<?> getTweetsLikedByUser(@PathVariable long id){
        validateId(id);
        return ResponseEntity.ok(likeService.findLikedTweetsByUserId(id));
    }


    @GetMapping("/search")
    public List<UserResponse> searchUsers(@RequestParam("q") String query) {
        return userService.searchUsers(query);
    }

}
