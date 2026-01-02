package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.ListMembers;
import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class ListsMemberRepoTest {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ListMembersRepo listMembersRepo;

    @Autowired
    private ListsRepo listsRepo;

    @BeforeEach
    void setup() {
        Users users1 = new Users();
        users1.setEmail("example1@example.com");
        users1.setUsername("user1");
        users1.setPassword("pass1");
        userRepo.save(users1);

        Users users2= new Users();
        users2.setEmail("example2@example.com");
        users2.setUsername("user2");
        users2.setPassword("pass2");
        userRepo.save(users2);

        Lists lists=new Lists();
        lists.setListName("sample list");
        lists.setPrivate(false);
        lists.setDescription("this is private list");
        lists.setMembersCount(1);
        lists.setUserId(users1);
        lists.setSubscriberCount(1);
        listsRepo.save(lists);

        ListMembers members=new ListMembers();
        members.setLists(lists);
        members.setUser(users2);
        listMembersRepo.save(members);

        ListMembers members2=new ListMembers();
        members2.setLists(lists);
        members2.setUser(users1);
        listMembersRepo.save(members2);
    }

    @Test
    public void testFindListMembersByUserAndLists(){
        Users users=userRepo.findByEmail("example1@example.com").orElseThrow();
        Lists list=listsRepo.findListsByUserId(users).getFirst();

        Optional<ListMembers>members=listMembersRepo.findListMembersByUserAndLists(users,list);
        assertFalse(members.isEmpty());

        ListMembers members1=members.get();
        assertEquals(members1.getUser(),members1.getLists().getUserId());
    }
}
