package com.saurabh.Social_Media_Backend.utils;

import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getCurrentUser(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth== null){
            throw new AppException(HttpStatus.UNAUTHORIZED.value(),"unauthorized");
        }
        return  (String) auth.getPrincipal();
    }
}
