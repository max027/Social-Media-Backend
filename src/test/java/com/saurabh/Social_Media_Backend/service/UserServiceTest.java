package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setup(){
        long id=1L;
        Users users=new Users();
        users.setUsername("max");
        users.setEmail("example@email.com");
        users.setUserId(1L);
        users.setPassword("Pass@321");
        users.setBio("this is bio");
        users.setBannerUrl("http://localhost:8080/banner");
        users.setProfilePictureUrl("http://localhost:8080/profile");
        users.setFollowersCount(100);
        users.setFollowersCount(400);
        users.setDateOfBirth(LocalDate.now());
        users.setVerified(true);
        Mockito.when(userRepo.findById(id))
                .thenReturn(Optional.of(users));
    }

    @Test
    public void testFindByUsername(){
        Users users=new Users();
        users.setUsername("max");
        users.setEmail("example@email.com");
        Mockito.when(userRepo.findByUsername("max"))
                .thenReturn(Optional.of(users));

        UserResponse userResponse=service.findByUsername("max".trim());
        assertNotNull(userResponse);
        assertEquals("max",userResponse.username());
        assertEquals("example@email.com",userResponse.email());

        assertThrows(AppException.class,()->service.findByUsername("sam"));
    }
    @Test
    public void testGetById(){
        UserResponse userResponse=service.findById(1L);
        assertNotNull(userResponse);
        assertEquals("max",userResponse.username());
        assertEquals("example@email.com",userResponse.email());

        assertThrows(AppException.class,()->service.findById(-1));
    }
    @Test
    public void testUpdateUser(){
       UserResponse userResponse=service.findById(1L);
        assertNotNull(userResponse);
        assertEquals("max",userResponse.username());
        assertEquals("example@email.com",userResponse.email());

        Users updateUser=new Users();
        updateUser.setUserId(userResponse.userId());
        updateUser.setUsername(userResponse.username());
        updateUser.setEmail("updateemail@email.com");
        updateUser.setBio("hello world");
        updateUser.setProfilePictureUrl(userResponse.profilePictureUrl());
        updateUser.setBannerUrl(userResponse.bannerUrl());
        updateUser.setDateOfBirth(userResponse.dateOfBirth());
        updateUser.setFollowersCount(userResponse.followersCount()+200);
        updateUser.setFollowingCount(userResponse.followingCount()-20);

        UserResponse userResponse2=service.updateUser(updateUser);
        assertEquals("updateemail@email.com",userResponse2.email());
        assertEquals(userResponse.followersCount()+200,userResponse2.followersCount());
        assertEquals(userResponse.followingCount()-20,userResponse2.followingCount());
    }

}
