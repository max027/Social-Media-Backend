package com.saurabh.Social_Media_Backend.repo;


import com.saurabh.Social_Media_Backend.dto.NotificationType;
import com.saurabh.Social_Media_Backend.models.Notification;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NotificationRepoTest {
   @Autowired
    private NotificatioinsRepo notificatioinsRepo;

   @Autowired
   private UserRepo userRepo;

   @Autowired
   private TweetsRepo tweetsRepo;



    @BeforeEach
    void setup() {
       Users users1 = new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);

        Users users2 = new Users();
        users2.setEmail("example2@example.com");
        users2.setUsername("user2");
        users2.setPassword("pass2");
        userRepo.save(users2);

        Tweets tweets=new Tweets();
        tweets.setContent("hello");
        tweets.setUsers(users1);
        tweetsRepo.save(tweets);

        Notification notification=new Notification();
        notification.setTweets(tweets);
        notification.setActors(users2);
        notification.setUsers(users1);
        notification.setNotificationType(NotificationType.LIKE);
        notificatioinsRepo.save(notification);
    }

    @Test
    public void testFindNotificationByUsers(){
        Users users1=userRepo.findByEmail("example1@example.com").orElseThrow();
        List<Notification>list=notificatioinsRepo.findNotificationByUsers(users1);
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());

        Users users2=userRepo.findByEmail("example2@example.com").orElseThrow();
        List<Notification>list2=notificatioinsRepo.findNotificationByUsers(users2);

        assertEquals(0,list2.size());
    }

}
