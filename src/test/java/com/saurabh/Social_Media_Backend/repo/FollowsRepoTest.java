package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Follows;
import com.saurabh.Social_Media_Backend.models.Likes;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FollowsRepoTest {

    @Autowired
    private UserRepo userRepo;

    private Users users1;
    private Users followToUser;

    @Autowired
    private FollowsRepo followsRepo;

    @BeforeEach
    void setup() {

        users1 = new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);

        followToUser = new Users();
        followToUser.setEmail("example2@example.com");
        followToUser.setUsername("user2");
        followToUser.setPassword("pass2");
        userRepo.save(followToUser);

        Follows follows=new Follows();
        follows.setFollowerId(users1);
        follows.setFollowingId(followToUser);
        followsRepo.save(follows);
    }

    @Test
    public void testFindFollowsByFollowerIdAndFollowingI(){
        Optional<Follows>follows1=followsRepo.findFollowsByFollowerIdAndFollowingId(users1,followToUser);

        assertFalse(follows1.isEmpty());
        Follows follows2=follows1.get();

        assertEquals(follows2.getFollowerId().getUserId(),users1.getUserId());
        assertEquals(follows2.getFollowingId().getUserId(),followToUser.getUserId());
    }

    @Test
    public void testFindUsersFollowers(){
        List<Users>list=followsRepo.findUsersFollowers(followToUser);
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());
    }

    @Test
    public void testFindUsersFollowing(){
        List<Users>list=followsRepo.findUsersFollowing(followToUser);
        assertTrue(list.isEmpty());
    }
}