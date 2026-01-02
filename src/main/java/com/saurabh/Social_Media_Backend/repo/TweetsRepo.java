package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Blocked;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetsRepo extends JpaRepository<Tweets,Long> {
    List<Tweets> findByUsersUserId(long id);

    Optional<Tweets> findTweetsByUsersAndOriginalTweetId(Users users, Tweets originalTweetId);

    Optional<Tweets> findTweetsByUsersAndQuoteTweetId(Users users, Tweets quoteTweetId);

    Optional<Tweets> findTweetsByUsersAndReplyToTweet(Users users, Tweets replyToTweet);

    @Query("""
        SELECT r FROM Tweets  r 
        WHERE r.quoteTweetId=:originalTweet
        AND NOT EXISTS (
            SELECT 1 FROM Blocked  b 
            WHERE (b.blockerId=r.quoteTweetId.users AND b.blockedId=:users)
            OR (b.blockerId=:users AND b.blockedId=r.quoteTweetId.users)
        )

""")
    List<Tweets> findTweetsByReplyToTweet(@Param("originalTweet") Tweets originalTweet,@Param("users") Users viewerId );


}
