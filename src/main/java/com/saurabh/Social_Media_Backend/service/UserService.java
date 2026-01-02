package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.DtoMapper;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.*;
import com.saurabh.Social_Media_Backend.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final FollowsRepo followsRepo;
    private final BlockRepo blockRepo;
    private final DtoMapper mapper=DtoMapper.getDtoMapper();
    private final getCurrentUserService getCurrentUserService;
    private final checkBlockedService checkBlockedService;
    private final TweetsRepo tweetsRepo;
    private final LikeRepo likeRepo;
    private final TweetsService tweetsService;

    public UserService(UserRepo repo, FollowsRepo followsRepo, BlockRepo blockRepo, getCurrentUserService getCurrentUserService, checkBlockedService checkBlockedService, TweetsRepo tweetsRepo, LikeRepo likeRepo, TweetsService tweetsService){
        this.userRepo =repo;
        this.blockRepo=blockRepo;
        this.followsRepo=followsRepo;
        this.getCurrentUserService=getCurrentUserService;
        this.checkBlockedService = checkBlockedService;
        this.tweetsRepo = tweetsRepo;
        this.likeRepo = likeRepo;
        this.tweetsService = tweetsService;
    }

    private UserResponse createResponse(Users users){
        return mapper.toUserResponse(users);
    }


    //find other user
    private Users getUserById(long id){
        Optional<Users>users= userRepo.findById(id);
        if (users.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "user with id:"+id+" not found");
        }
        return users.get();
    }


    public UserResponse findByUsername(String username){
        Users users= userRepo.findByUsername(username).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "user not found")
        );
        if (users==null){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "user with username:"+username+" not found");
        }
        return createResponse(users);
    }

    public UserResponse findById(long id){
        Users users=getUserById(id);
        return createResponse(users);
    }

    @Transactional
    public UserResponse updateUser(Users users){
        Users fetch_user=getCurrentUserService.getCurrentUser();
        users.setUserId(fetch_user.getUserId());
        return createResponse(userRepo.save(users));
    }

    @Transactional
    public void deleteUser(){
        Users users= getCurrentUserService.getCurrentUser();
        userRepo.deleteById(users.getUserId());
    }


    public List<UserResponse> searchUsers(String query) {
        List<Users>usersList= userRepo.searchByUsernameOrName(query);
        List<UserResponse>list=new ArrayList<>();
        for (Users users:usersList){
            UserResponse userResponse=createResponse(users);
            list.add(userResponse);
        }
        return list;
    }

    public void followUsers(long id){
        Users followers=getCurrentUserService.getCurrentUser();
        Users following=getUserById(id);

        //if the user that you are following has blocked you cannot follow
        Optional<Blocked>blocked=blockRepo.findBlockedByBlockedIdAndBlockerId(followers,following);

        blocked.ifPresent(value->{
            throw  new AppException(HttpStatus.FORBIDDEN.value(), "user has blocked you");
        });

        Follows follows=new Follows();
        follows.setFollowerId(followers);
        follows.setFollowingId(following);

        following.setFollowersCount(followers.getFollowersCount()+1);

        followsRepo.save(follows);
    }

    public void unFollowUsers(long id){
        Users followers=getCurrentUserService.getCurrentUser();
        Users following=getUserById(id);
        Follows follows=followsRepo.findFollowsByFollowerIdAndFollowingId(followers,following).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "user do not follow:"+id)
        );

        following.setFollowersCount(followers.getFollowersCount()-1);

        followsRepo.delete(follows);
    }

    public List<UserResponse> getFollowers(long id){
        Users users=getUserById(id);
        List<Users>list=followsRepo.findUsersFollowers(users);
        return list.stream().map(mapper::toUserResponse).toList();
    }

    public List<UserResponse> getFollowing(long id){
        Users users=getUserById(id);
        List<Users>list=followsRepo.findUsersFollowing(users);
        return list.stream().map(mapper::toUserResponse).toList();
    }

    public void blockUser(long id){
        Users users=getCurrentUserService.getCurrentUser();
        Users blockedUser=getUserById(id);

        //check if users follow blockedUser and vise versa and unfollow
        Optional<Follows> userFollowBlocked=followsRepo.findFollowsByFollowerIdAndFollowingId(users,blockedUser);
        Optional<Follows> blockedFollowUser=followsRepo.findFollowsByFollowerIdAndFollowingId(blockedUser,users);

        if (userFollowBlocked.isPresent() || blockedFollowUser.isPresent()){
            followsRepo.deleteMutualFollows(users,blockedUser);
        }

        //if user liked blocked user tweets unlike them
        List<Tweets> likedBlockedTweets=tweetsRepo.findByUsersUserId(blockedUser.getUserId());
        for(Tweets tweets:likedBlockedTweets){
            Optional<Likes>likes=likeRepo.findLikesByTweetsAndUsers(tweets,blockedUser);
            if (likes.isPresent()){
                tweetsService.unlikeTweet(tweets.getTweetsId());
            }
        }
        //if user retweet blocked user tweet undo them



        Blocked blocked=new Blocked();
        blocked.setBlockedId(blockedUser);
        blocked.setBlockerId(users);

        blockRepo.save(blocked);
    }
    public void unBlockUser(long id){
        Users users=getCurrentUserService.getCurrentUser();
        Users blockedUser=getUserById(id);

        Blocked blocked=blockRepo.findBlockedByBlockedIdAndBlockerId(blockedUser,users).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "not found")
        );

        blockRepo.delete(blocked);
    }
    public List<UserResponse>getBlockedList(){
        Users users=getCurrentUserService.getCurrentUser();
        List<Users>list=blockRepo.findUsersBlockedList(users);
        return list.stream().map(mapper::toUserResponse).toList();
    }


}

