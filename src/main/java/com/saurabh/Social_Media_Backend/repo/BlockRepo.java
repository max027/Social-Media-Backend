package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Blocked;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepo extends CrudRepository<Blocked,Long> {
    Optional<Blocked>findBlockedByBlockedIdAndBlockerId(Users blockedId, Users blockerId);

    @Query("SELECT b.blockedId FROM Blocked b WHERE b.blockerId=:users")
    List<Users> findUsersBlockedList(@Param("users") Users users);
}
