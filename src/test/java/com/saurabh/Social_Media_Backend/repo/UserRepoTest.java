package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setup(){
        Users users1=new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);

        Users users2=new Users();
        users2.setEmail("example2@example.com");
        users2.setUsername("user2");
        users2.setPassword("pass2");
        userRepo.save(users2);

        Users users3=new Users();
        users3.setEmail("example3@example.com");
        users3.setUsername("user3");
        users3.setPassword("pass3");
        userRepo.save(users3);
    }

    @Test
    public void shouldSave(){
        Users users=new Users();
        users.setUsername("sample");
        users.setPassword("pass");
        users.setEmail("example1@example.com");

        Users saved=userRepo.save(users);

        assertNotNull(saved.getUserId());
    }

    @Test
    public void testFindByEmail(){
        String email="example1@example.com";
        Users users=userRepo.findByEmail("example1@example.com").get();
        assertEquals(email,users.getEmail());


        String email2="example2@example.com";
        Users users2=userRepo.findByEmail("example2@example.com").get();
        assertEquals(email,users.getEmail());

        assertThrows(UsernameNotFoundException.class,
                ()->userRepo.findByEmail("example4@email.com").orElseThrow(
                        ()->new UsernameNotFoundException("")
                ));
    }

    @Test
    public void testFindUsersWhoRetweeted(){
       List<Users> list=userRepo.searchByUsernameOrName("example");
       assertEquals(3,list.size());

       List<Users>list2=userRepo.searchByUsernameOrName("hello");
       assertEquals(0,list2.size());
       assertTrue(list2.isEmpty());
    }

    @Test
    public void testFindByUsername(){
        Users users=userRepo.findByUsername("user1").get();
        assertEquals("example1@example.com",users.getEmail());
        assertEquals("user1",users.getUsername());

        assertNull(userRepo.findByUsername("sam"));
    }
}
