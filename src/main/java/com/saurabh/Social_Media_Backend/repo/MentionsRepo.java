package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Mentions;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MentionsRepo extends CrudRepository<Mentions,Long> {
    List<Mentions> findMentionsByUsers(Users users);
}
