package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LikeRepoTest {
    @Autowired
    private LikeRepo likeRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TweetsRepo tweetsRepo;

    private Tweets tweets;


    @BeforeEach
    void test(){

       Users users1=new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);

        Users users2=new Users();
        users2.setEmail("example2@example.com");
        users2.setUsername("user2");
        users2.setPassword("pass1");
        userRepo.save(users2);

        tweets=new Tweets();
        tweets.setContent("This is sample test");
        tweets.setBookmarkCount(12);
        tweets.setRetweet(false);
        tweets.setQuote(false);
        tweets.setUsers(users1);
        tweets.setLikeCount(200);
        tweetsRepo.save(tweets);

        Likes likes=new Likes();
        likes.setUsers(users1);
        likes.setTweets(tweets);
        likeRepo.save(likes);

    }
    @Test
    void testFindTweetsLikedByUser(){
        Users users1=userRepo.findByEmail("example1@example.com").orElseThrow();
        List<Tweets> list=likeRepo.findTweetsLikedByUser(users1.getUserId());
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());


        Users users2=userRepo.findByEmail("example2@example.com").orElseThrow();
        List<Tweets> list2=likeRepo.findTweetsLikedByUser(users2.getUserId());
        assertTrue(list2.isEmpty());
    }
    @Test
    void findLikesByTweetsAndUsers(){
        Users users1=userRepo.findByEmail("example1@example.com").orElseThrow();
        Optional<Likes> list=likeRepo.findLikesByTweetsAndUsers(tweets,users1);
        assertFalse(list.isEmpty());

        Likes likes1=list.get();
        assertEquals(likes1.getUsers(),users1);
        assertEquals(likes1.getTweets(),tweets);
    }
    @Test
    void findUsersWhoLikedTweet(){
        Tweets tweets=tweetsRepo.findById(1L).orElseThrow();
        Users users2=userRepo.findByEmail("example2@example.com").orElseThrow();

        List<Users>users=likeRepo.findUsersWhoLikedTweet(tweets.getTweetsId());
        assertFalse(users.isEmpty());
        assertEquals(1,users.size());

        Tweets tweets1=new Tweets();
        tweets1.setUsers(users2);
        tweets.setContent("hello");
        tweetsRepo.save(tweets1);
        List<Users>users4=likeRepo.findUsersWhoLikedTweet(tweets1.getTweetsId());
        assertTrue(users4.isEmpty());
    }
}
