package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.dto.UsersMapper;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo repo;
    private final UsersMapper mapper= Mappers.getMapper(UsersMapper.class);

    public UserService(UserRepo repo){
        this.repo=repo;
    }

    public UserResponse createResponse(Users users){
        return mapper.toUserResponse(users);
    }

    private Users checkUserExistById(long id){
        Optional<Users>users=repo.findById(id);
        if (users.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "user with id:"+id+" not found");
        }
        return users.get();
    }

    public UserResponse findByUsername(String username){
        Users users=repo.findByUsername(username);
        if (users==null){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "user with username:"+username+" not found");
        }
        return createResponse(users);
    }

    public UserResponse findById(long id){
        Users users= checkUserExistById(id);
        return createResponse(users);
    }

    @Transactional
    public UserResponse updateById(long id,Users users){
        Users fetch_user= checkUserExistById(id);
        users.setUserId(fetch_user.getUserId());
        return createResponse(repo.save(users));
    }

    @Transactional
    public void deleteById(long id){
        Users users= checkUserExistById(id);
        repo.deleteById(id);
    }


    public List<UserResponse> searchUsers(String query) {
        List<Users>usersList=repo.searchByUsernameOrName(query);
        List<UserResponse>list=new ArrayList<>();
        for (Users users:usersList){
            UserResponse userResponse=createResponse(users);
            list.add(userResponse);
        }
        return list;
    }

}
