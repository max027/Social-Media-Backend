package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.dto.TweetsResponse;
import com.saurabh.Social_Media_Backend.models.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetsRepo extends JpaRepository<Tweets,Long> {
    List<Tweets> findByUsersUserId(long id);
}
