package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.LoginUserDto;
import com.saurabh.Social_Media_Backend.repo.RegisterUserDto;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
   private final UserRepo userRepo;
   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager authenticationManager;

   private AuthService(UserRepo userRepo,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager){
       this.authenticationManager=authenticationManager;
       this.userRepo=userRepo;
       this.passwordEncoder=passwordEncoder;
   }

   public Users signup(RegisterUserDto registerUserDto){
       Users users=new Users();
       users.setEmail(registerUserDto.getEmail());
       users.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
       users.setUsername(registerUserDto.getUsername());

       return userRepo.save(users);
   }

   public Users authenticate(LoginUserDto input){
       UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword());
       Authentication authentication=authenticationManager.authenticate(token);
       return (Users) authentication.getPrincipal();
   }
}
