package com.saurabh.Social_Media_Backend.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private String bio;
    private String profilePictureUrl;
    private String bannerUrl;
    private LocalDate dateOfBirth;
    private boolean isVerified;
    private int followersCount;
    private int followingCount;

}
