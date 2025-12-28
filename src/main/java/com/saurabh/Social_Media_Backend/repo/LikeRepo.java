package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepo extends JpaRepository<Likes,Long> {

    @Query("""
    SELECT l.tweets_id
    FROM Likes l
    WHERE l.users.userId = :userId
""")
    List<Tweets> findTweetsLikedByUser(@Param("userId") long id);

}
