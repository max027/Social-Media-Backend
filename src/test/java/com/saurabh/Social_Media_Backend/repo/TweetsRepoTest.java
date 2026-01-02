package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class TweetsRepoTest {
    @Autowired
    private TweetsRepo tweetsRepo;

    @Autowired
    private UserRepo userRepo;

    private Tweets retweet;
    private Users retweetUser;
    private Users quoteUser;
    private Tweets quote;
    private Tweets tweets;

    @BeforeEach
    void setup(){
        Users users1=new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);

        retweetUser =new Users();
        retweetUser.setEmail("example2@example.com");
        retweetUser.setUsername("user2");
        retweetUser.setPassword("pass2");
        userRepo.save(retweetUser);

        tweets=new Tweets();
        tweets.setContent("This is sample test");
        tweets.setBookmarkCount(12);
        tweets.setRetweet(false);
        tweets.setQuote(false);
        tweets.setUsers(users1);
        tweets.setLikeCount(200);
        tweetsRepo.save(tweets);


        retweet =new Tweets();
        retweet.setContent("This is sample test 2");
        retweet.setBookmarkCount(10);
        retweet.setRetweet(false);
        retweet.setQuote(false);
        retweet.setUsers(users1);
        retweet.setLikeCount(100);
        tweetsRepo.save(retweet);

        Tweets tweets2=new Tweets();
        tweets2.setUsers(retweetUser);
        tweets2.setContent("");
        tweets2.setRetweet(true);
        tweets2.setOriginalTweetId(retweet);
        tweetsRepo.save(tweets2);

        quoteUser=new Users();
        quoteUser.setEmail("example4@example.com");
        quoteUser.setUsername("quoteUser");
        quoteUser.setPassword("pass5");
        userRepo.save(quoteUser);

        quote=new Tweets();
        quote.setUsers(quoteUser);
        quote.setContent("");
        quote.setQuote(true);
        quote.setQuoteTweetId(tweets);
        tweetsRepo.save(quote);

    }

    @Test
    public void testUsersUserId(){
        long id=1L;
        List<Tweets>list=tweetsRepo.findByUsersUserId(id);
        assertNotEquals(0,list.size());
        assertEquals(2,list.size());

        List<Tweets>list1=tweetsRepo.findByUsersUserId(2L);
        assertEquals(0,list1.size());

        assertEquals("This is sample test",list.getFirst().getContent());
    }

    @Test
    public void testFindTweetsByUsersAndOriginalTweetId(){
        Tweets tweets=tweetsRepo.findTweetsByUsersAndOriginalTweetId(retweetUser, retweet).orElseThrow();

        assertTrue(tweets.isRetweet());
        assertFalse(tweets.isQuote());
        assertEquals(retweetUser.getUserId(),tweets.getUsers().getUserId());
        assertEquals(retweet.getTweetsId(),tweets.getOriginalTweetId().getTweetsId());
    }
    @Test
    public void testFindTweetsByUsersAndQuoteTweetId(){
        assertTrue(quote.isQuote());
        assertFalse(quote.isRetweet());
        assertEquals(quoteUser.getUserId(),quote.getUsers().getUserId());
        assertEquals(quote.getQuoteTweetId().getTweetsId(),tweets.getTweetsId());
    }

}
