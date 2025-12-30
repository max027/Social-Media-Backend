package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
    Users findByUsername(String username);

    Optional<Users> findByEmail(String email);


    @Query("SELECT u FROM Users u WHERE u.username LIKE %:query% OR u.email LIKE %:query%")
    List<Users> searchByUsernameOrName(@Param("query") String query);

    @Query("SELECT t.users FROM Tweets t WHERE t.originalTweetId=:tweetId")
    List<Users> findUsersWhoRetweeted(@Param("tweetId") Tweets tweets);


    @Query("SELECT t.users FROM Tweets t WHERE t.quoteTweetId=:tweets")
    List<Users> findUsersWhoQuoted(@Param("tweets") Tweets tweets);

}
