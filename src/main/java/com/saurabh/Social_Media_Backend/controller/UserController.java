package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
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

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable  String username){
        UserResponse users= userService.findByUsername(username);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable long id) throws ResponseStatusException {
        UserResponse users = userService.findById(id);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/")
    public ResponseEntity<UserResponse> updateById(@RequestBody Users users ){
        UserResponse updated_user= userService.updateUser(users);
        return ResponseEntity.ok(updated_user);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteById(){
       userService.deleteUser();
       return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/tweets")
    public ResponseEntity<List<TweetsResponse>> getTweetsByUserId(@PathVariable  long id){
        return ResponseEntity.ok(tweetsService.findByUserId(id));
    }

    @GetMapping("/id/{id}/likes")
    public ResponseEntity<?> getTweetsLikedByUser(@PathVariable long id){
        return ResponseEntity.ok(likeService.findLikedTweetsByUserId(id));
    }


    @GetMapping("/search")
    public List<UserResponse> searchUsers(@RequestParam("q") String query) {
        return userService.searchUsers(query);
    }

    @PostMapping("/id/{id}/follow")
    public ResponseEntity<?> followUsers(@PathVariable long id){
        userService.followUsers(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/id/{id}/follow")
    public ResponseEntity<?> unfollowUsers(@PathVariable long id){
        userService.unFollowUsers(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable long id){
        return ResponseEntity.ok(userService.getFollowers(id));
    }

    @GetMapping("/id/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable long id){

        return ResponseEntity.ok(userService.getFollowing(id));
    }

    @PostMapping("/id/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable long id){
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/id/{id}/block")
    public ResponseEntity<?> unBlockUser(@PathVariable long id){
        userService.unBlockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/block")
    public ResponseEntity<List<UserResponse>> getBlockList(){

        return ResponseEntity.ok(userService.getBlockedList());
    }
}
