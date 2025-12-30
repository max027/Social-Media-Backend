package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Follows;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowsRepo extends CrudRepository<Follows,Long> {
    Follows findFollowsByFollowerIdAndFollowingId(Users followerId, Users followingId);

    @Query("SELECT f.followerId FROM Follows f WHERE f.followingId= :users")
    List<Users> findUsersFollowers(@Param("users")Users users);

    @Query("SELECT f.followingId FROM Follows f WHERE f.followerId= :users")
    List<Users> findUsersFollowing(@Param("users")Users users);

}
