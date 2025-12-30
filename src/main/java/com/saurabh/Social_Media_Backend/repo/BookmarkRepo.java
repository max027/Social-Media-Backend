package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Bookmarks;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepo extends CrudRepository<Bookmarks,Long> {
    Bookmarks findBookmarksByUsersAndTweetsId(Users users, Tweets tweetsId);

    List<Bookmarks> findBookmarksByUsers(Users users);
}
