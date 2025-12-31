package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.DtoMapper;
import com.saurabh.Social_Media_Backend.dto.ListsRequest;
import com.saurabh.Social_Media_Backend.dto.ListsResponse;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.ListMembers;
import com.saurabh.Social_Media_Backend.models.ListSubscribers;
import com.saurabh.Social_Media_Backend.models.Lists;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.ListMembersRepo;
import com.saurabh.Social_Media_Backend.repo.ListsRepo;
import com.saurabh.Social_Media_Backend.repo.ListsSubscriberRepo;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListsService {
    private final ListsRepo listsRepo;
    private final getCurrentUserService getCurrentUserService;
    private final DtoMapper mapper=DtoMapper.getDtoMapper();
    private final ListMembersRepo listMembersRepo;
    private final UserRepo userRepo;
    private final ListsSubscriberRepo listsSubscriberRepo;

    public ListsService(ListsRepo listsRepo, getCurrentUserService getCurrentUserService, ListMembersRepo listMembersRepo, UserRepo userRepo, ListsSubscriberRepo listsSubscriberRepo){
        this.listsRepo=listsRepo;
        this.getCurrentUserService=getCurrentUserService;
        this.listMembersRepo=listMembersRepo;
        this.userRepo = userRepo;
        this.listsSubscriberRepo = listsSubscriberRepo;
    }

    private Lists checkId(long id){
        Optional<Lists> lists= listsRepo.findById(id);
        if (lists.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "lists with id:"+id+" not found");
        }
        return lists.get();
    }
    private void checkOwnership(Users users,Lists lists){
        long userId=users.getUserId();
        long listsId=lists.getUserId().getUserId();
        if (userId!=listsId){
            throw new AppException(HttpStatus.FORBIDDEN.value(), "Forbidden");
        }
    }
    public ListsResponse createList(ListsRequest lists){
        if (lists==null){
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "list is empty");
        }
        Lists lists1=new Lists();
        Users users=getCurrentUserService.getCurrentUser();
        lists1.setUserId(users);
        lists1.setListName(lists.listName());
        lists1.setDescription(lists.description());
        lists1.setPrivate(lists.isPrivate());

        listsRepo.save(lists1);

        return mapper.toListsResponse(lists1);
    }

    public List<ListsResponse> getLists(){
        Users users=getCurrentUserService.getCurrentUser();
        List<Lists>lists=listsRepo.findListsByUserId(users);
        return lists.stream().map(mapper::toListsResponse).toList();
    }

    public ListsResponse getById(long id){
       Lists lists=checkId(id);
       return mapper.toListsResponse(lists);
    }

    public ListsResponse updateById(long id,ListsRequest request){
        if (request==null){
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "list is empty");
        }
        Lists lists=checkId(id);
        Users users=getCurrentUserService.getCurrentUser();

        checkOwnership(users,lists);

        Lists lists1=new Lists();
        lists1.setUserId(users);
        lists1.setListId(id);
        lists1.setListName(request.listName());
        lists1.setDescription(request.description());
        lists1.setPrivate(lists.isPrivate());
        listsRepo.save(lists1);
        return mapper.toListsResponse(lists);
    }

    public void deleteById(long id){
        Lists lists=checkId(id);
        Users users=getCurrentUserService.getCurrentUser();

        checkOwnership(users,lists);
        listsRepo.deleteById(lists.getListId());
    }

    public void addMembers(long listId,long memberId){
        Users users=getCurrentUserService.getCurrentUser();
        Lists lists=checkId(listId);
        checkOwnership(users,lists);
        Users member=userRepo.findById(memberId).orElseThrow(
                ()->new UsernameNotFoundException("user not found")
        );

        ListMembers members=new ListMembers();
        members.setLists(lists);
        members.setUser(member);
        listMembersRepo.save(members);
    }

    public void deleteMembers(long listId,long memberId){
        Users users=getCurrentUserService.getCurrentUser();
        Lists lists=checkId(listId);
        checkOwnership(users,lists);

        Users member=userRepo.findById(memberId).orElseThrow(
                ()->new UsernameNotFoundException("member not found")
        );
        ListMembers members=listMembersRepo.findListMembersByUserAndLists(member,lists).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "list member does not countain member:"+memberId)
        );

        listsRepo.deleteById(members.getListMemberId());
    }
    public List<UserResponse>getListMembers(long id){
        Lists list=checkId(id);
        Users users=getCurrentUserService.getCurrentUser();
        if(list.isPrivate()){
            checkOwnership(users,list);
            List<Users>usersLists=listsRepo.findMembersOfList(list);
            return usersLists.stream().map(mapper::toUserResponse).toList();
        }else {
            List<Users>usersLists=listsRepo.findMembersOfList(list);
            return usersLists.stream().map(mapper::toUserResponse).toList();
        }
    }

    // subscriber
    public void addSubscriber(long id){
        Lists lists=checkId(id);
        Users users=getCurrentUserService.getCurrentUser();
        ListSubscribers subscribers=new ListSubscribers();
        subscribers.setLists(lists);
        subscribers.setUsers(users);

        listsSubscriberRepo.save(subscribers);

    }

    public void deleteSubscriber(long id){
        Lists lists=checkId(id);
        Users users=getCurrentUserService.getCurrentUser();
        ListSubscribers subscribers=listsSubscriberRepo.findListSubscribersByUsersAndLists(users,lists).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "user is not subscribed to list")
        );
        listsSubscriberRepo.delete(subscribers);
    }

    // subscriber
    public List<ListsResponse>getSubscribers(long id){
        Lists lists=checkId(id);
        List<Lists>subscribers=listsSubscriberRepo.findSubscribers(lists);
        return subscribers.stream().map(mapper::toListsResponse).toList();
    }
}
