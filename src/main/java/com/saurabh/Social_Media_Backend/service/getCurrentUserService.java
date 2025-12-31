package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import com.saurabh.Social_Media_Backend.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class getCurrentUserService {

    private final UserRepo userRepo;
    public getCurrentUserService(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    public Users getCurrentUser(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth== null){
            throw new AppException(HttpStatus.UNAUTHORIZED.value(),"unauthorized");
        }
        String email=(String) auth.getPrincipal();
        return userRepo.findByEmail(SecurityUtils.getCurrentUser()).orElseThrow(
                ()-> new UsernameNotFoundException("user not found")
        );
    }
}
