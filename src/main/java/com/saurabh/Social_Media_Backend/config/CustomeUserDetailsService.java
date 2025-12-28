package com.saurabh.Social_Media_Backend.config;

import com.saurabh.Social_Media_Backend.repo.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

public class CustomeUserDetailsService implements UserDetailsService {


    private final UserRepo userRepo;
    public CustomeUserDetailsService(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading User:"+email);
        return userRepo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("user with :"+email+"not found"));
    }
}
