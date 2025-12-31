package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.dto.ListsResponse;
import com.saurabh.Social_Media_Backend.models.ListMembers;
import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListsRepo extends CrudRepository<Lists,Long> {
    List<Lists> findListsByUserId(Users userId);

    @Query("""
            SELECT u FROM Users  u 
            JOIN ListMembers lm ON lm.user.userId=u.userId 
            WHERE lm.lists=:lists
            """)
    List<Users> findMembersOfList(@Param("lists") Lists lists);
}
