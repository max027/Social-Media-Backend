package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetsRepo extends JpaRepository<Tweets,Long> {
    List<Tweets> findByUsersUserId(long id);

    //find retweet by userIdan originalTweetId
    Optional<Tweets> findTweetsByUsersAndOriginalTweetId(Users users, Tweets originalTweetId);

    Optional<Tweets> findTweetsByUsersAndQuoteTweetId(Users users, Tweets quoteTweetId);

    Optional<Tweets> findTweetsByUsersAndReplyToTweet(Users users, Tweets replyToTweet);

    List<Tweets> findTweetsByReplyToTweet(Tweets replyToTweet);


}
