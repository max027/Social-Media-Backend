package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Follows;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowsRepo extends CrudRepository<Follows,Long> {
    Optional<Follows>findFollowsByFollowerIdAndFollowingId(Users followerId, Users followingId);

    @Query("""
        DELETE FROM Follows  f
        WHERE (f.followerId=:user1 AND f.followingId=:user2)
        OR (f.followerId=:user2 AND f.followingId=:user1)
        """)
    void deleteMutualFollows(@Param("user1") Users user1, @Param("user2") Users user2);

    @Query("SELECT f.followerId FROM Follows f WHERE f.followingId= :users")
    List<Users> findUsersFollowers(@Param("users")Users users);

    @Query("SELECT f.followingId FROM Follows f WHERE f.followerId= :users")
    List<Users> findUsersFollowing(@Param("users")Users users);

}
