package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Likes,Long> {

    @Query("""
    SELECT l.tweets.tweetsId
    FROM Likes l
    WHERE l.users.userId = :userId
""")
    List<Tweets> findTweetsLikedByUser(@Param("userId") long id);

    Optional<Likes> findLikesByTweetsAndUsers(Tweets tweets, Users users);

    @Query("SELECT l.users FROM Likes  l WHERE l.tweets.tweetsId=:tweetId")
    List<Users> findUsersWhoLikedTweet(@Param("tweetId") long tweets);
}
