package com.saurabh.Social_Media_Backend.dto;


import java.time.LocalDate;

public record UserResponse(
    Long userId,
    String username,
    String email,
    String bio,
    String profilePictureUrl,
    String bannerUrl,
    LocalDate dateOfBirth,
//    boolean isVerified,
    int followersCount,
    int followingCount

){}
