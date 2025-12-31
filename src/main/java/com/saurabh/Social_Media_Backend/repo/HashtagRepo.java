package com.saurabh.Social_Media_Backend.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.saurabh.Social_Media_Backend.models.Hashtag;

@Repository
public interface HashtagRepo extends CrudRepository<Hashtag, Long> {
    Optional<Hashtag> findByTag(String tag);

    List<Hashtag> findHashtagByTag(String tag);
}
