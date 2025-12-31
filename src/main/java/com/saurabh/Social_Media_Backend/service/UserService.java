package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.DtoMapper;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Blocked;
import com.saurabh.Social_Media_Backend.models.Follows;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.BlockRepo;
import com.saurabh.Social_Media_Backend.repo.FollowsRepo;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import com.saurabh.Social_Media_Backend.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public UserService(UserRepo repo,FollowsRepo followsRepo,BlockRepo blockRepo,getCurrentUserService getCurrentUserService){
        this.userRepo =repo;
        this.blockRepo=blockRepo;
        this.followsRepo=followsRepo;
        this.getCurrentUserService=getCurrentUserService;
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
        Users users= userRepo.findByUsername(username);
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
    public UserResponse updateById(Users users){
        Users fetch_user=getCurrentUserService.getCurrentUser();
        users.setUserId(fetch_user.getUserId());
        return createResponse(userRepo.save(users));
    }

    @Transactional
    public void deleteById(){
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
        Follows follows=new Follows();
        follows.setFollowerId(followers);
        follows.setFollowingId(following);

        following.setFollowersCount(followers.getFollowersCount()+1);

        followsRepo.save(follows);
    }

    public void unFollowUsers(long id){
        Users followers=getCurrentUserService.getCurrentUser();
        Users following=getUserById(id);
        Follows follows=followsRepo.findFollowsByFollowerIdAndFollowingId(followers,following);

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

        Blocked blocked=new Blocked();
        blocked.setBlockedId(blockedUser);
        blocked.setBlockerId(users);

        blockRepo.save(blocked);
    }
    public void unBlockUser(long id){
        Users users=getCurrentUserService.getCurrentUser();
        Users blockedUser=getUserById(id);

        Blocked blocked=blockRepo.findBlockedByBlockedIdAndBlockerId(blockedUser,users);

        blockRepo.delete(blocked);
    }
    public List<UserResponse>getBlockedList(){
        Users users=getCurrentUserService.getCurrentUser();
        List<Users>list=blockRepo.findUsersBlockedList(users);
        return list.stream().map(mapper::toUserResponse).toList();
    }


}

