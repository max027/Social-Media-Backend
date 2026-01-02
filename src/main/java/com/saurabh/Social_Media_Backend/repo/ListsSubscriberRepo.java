package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.ListSubscribers;
import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListsSubscriberRepo extends CrudRepository<ListSubscribers,Long> {
    Optional<ListSubscribers> findListSubscribersByUsersAndLists(Users users, Lists lists);

    @Query("""
        SELECT u FROM Users  u
        JOIN  ListSubscribers  ls ON ls.users.userId=u.userId
        WHERE ls.lists=:lists
       """)
    List<Users> findSubscribers(@Param("lists") Lists lists);
}
