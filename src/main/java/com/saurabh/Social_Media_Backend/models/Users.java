package com.saurabh.Social_Media_Backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false,unique = true,name = "username")
    private String username;

    @Column(nullable = false,unique = true,name = "email")
    private String email;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Tweets> tweets;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ColumnDefault("false")
    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "followers_count")
    @ColumnDefault("0")
    private int followersCount;

    @ColumnDefault("0")
    @Column(name = "following_count")
    private int followingCount;

//    @ColumnDefault("0")
//    @Column(name = "tweets_count")
//    private int tweetsCount;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String password;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
