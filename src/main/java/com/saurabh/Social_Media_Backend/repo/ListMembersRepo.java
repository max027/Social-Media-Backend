package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.ListMembers;
import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListMembersRepo extends CrudRepository<ListMembers,Long> {

    Optional<ListMembers> findListMembersByUserAndLists(Users userId, Lists listsId);

}
