package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.ListMembers;
import com.saurabh.Social_Media_Backend.models.ListSubscribers;
import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ListSubscriberRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ListMembersRepo listMembersRepo;

    @Autowired
    private ListsRepo listsRepo;

    @Autowired ListsSubscriberRepo listsSubscriberRepo;

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

        Lists lists = new Lists();
        lists.setListName("sample list");
        lists.setPrivate(false);
        lists.setDescription("this is private list");
        lists.setMembersCount(1);
        lists.setUserId(users2);
        lists.setSubscriberCount(1);
        listsRepo.save(lists);

        ListSubscribers subscribers=new ListSubscribers();
        subscribers.setUsers(users2);
        subscribers.setLists(lists);
        listsSubscriberRepo.save(subscribers);
    }

    @Test
    public void testFindListSubscribersByUsersAndLists(){
        Users users=userRepo.findByEmail("example2@example.com").orElseThrow();
        Lists lists=listsRepo.findListsByUserId(users).getFirst();

        Optional<ListSubscribers> subscribers=listsSubscriberRepo.findListSubscribersByUsersAndLists(users,lists);

        assertFalse(subscribers.isEmpty());
        ListSubscribers subscribers1=subscribers.get();
        assertEquals(subscribers1.getUsers(),users);
        assertEquals(subscribers1.getLists(),lists);

    }
    @Test
    public void testSubscriber(){
        Users users=userRepo.findByEmail("example2@example.com").orElseThrow();

        Lists lists=listsRepo.findListsByUserId(users).getFirst();

        List<Users> usersLists=listsSubscriberRepo.findSubscribers(lists);

        assertFalse(usersLists.isEmpty());
        Users users1=usersLists.getFirst();

        assertEquals(users,users1);

    }
}
