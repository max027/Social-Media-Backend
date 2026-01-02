package com.saurabh.Social_Media_Backend.repo;


import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Bookmarks;
import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookmarkRepoTest {
    @Autowired
    private BookmarkRepo bookmarkRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TweetsRepo tweetsRepo;

    private Users users1;
    private Tweets tweets;
    private Bookmarks bookmarks;

    @BeforeEach
    void setup(){
        users1 = new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);


        tweets=new Tweets();
        tweets.setContent("hello");
        tweets.setUsers(users1);
        tweetsRepo.save(tweets);

        bookmarks=new Bookmarks();
        bookmarks.setUsers(users1);
        bookmarks.setTweetsId(tweets);
        bookmarkRepo.save(bookmarks);
    }

    @Test
    public  void testFindBookmarksByUsersAndTweetsId(){
       Optional<Bookmarks> bookmarks1=bookmarkRepo.findBookmarksByUsersAndTweetsId(users1,tweets);
        assertFalse(bookmarks1.isEmpty());
        Bookmarks bookmarks2=bookmarks1.get();
        assertEquals(bookmarks2.getTweetsId().getTweetsId(),tweets.getTweetsId());
        assertEquals(bookmarks2.getUsers().getUserId(),users1.getUserId());

        Tweets tweets2=new Tweets();
        tweets2.setContent("hello tweets 2");
        tweets2.setUsers(users1);
        tweetsRepo.save(tweets2);

        assertThrows(
                AppException.class, ()->bookmarkRepo.findBookmarksByUsersAndTweetsId(users1,tweets2)
                        .orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "")
        ));
    }

    @Test
    public  void testFindBookmarksByUsers(){
        List<Bookmarks>list=bookmarkRepo.findBookmarksByUsers(users1);
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());

        Users users2=new Users();
        users2.setEmail("example@email.com");
        users2.setUsername("sam");
        users2.setPassword("pass");
        userRepo.save(users2);
        List<Bookmarks>list2=bookmarkRepo.findBookmarksByUsers(users2);
        assertEquals(0,list2.size());
    }
}
