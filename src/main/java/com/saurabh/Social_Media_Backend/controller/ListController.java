package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.ListsRequest;
import com.saurabh.Social_Media_Backend.dto.ListsResponse;
import com.saurabh.Social_Media_Backend.dto.UserResponse;
import com.saurabh.Social_Media_Backend.service.ListsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {
    private final ListsService listsService;
    public ListController(ListsService listsService){
        this.listsService=listsService;
    }

    @PostMapping("/")
    public ResponseEntity<ListsResponse> createLists(@RequestBody ListsRequest request){
        ListsResponse response=listsService.createList(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<ListsResponse>> getLists(){

        return ResponseEntity.ok(listsService.getLists());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ListsResponse> getListsDetails(@PathVariable long id){
        return ResponseEntity.ok(listsService.getById(id));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<ListsResponse> updateLists(@PathVariable long id,@RequestBody ListsRequest listsRequest){
        return ResponseEntity.ok(listsService.updateById(id,listsRequest));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteLists(@PathVariable long id){
        listsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //list members
    @PostMapping("/id/{listId}/members/id/{memberId}")
    public ResponseEntity<?> addMembers(@PathVariable long listId,@PathVariable long memberId){
        listsService.addMembers(listId,memberId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/id/{listId}/members/id/{memberId}")
    public ResponseEntity<?> deleteMembers(@PathVariable long listId,@PathVariable long memberId){
        listsService.deleteMembers(listId,memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/members")
    public ResponseEntity<List<UserResponse>> getListMembers(@PathVariable long id){

        return ResponseEntity.ok(listsService.getListMembers(id));
    }
    //list subscribers
    @PostMapping("/id/{listId}/subscribe")
    public ResponseEntity<?> addSubscriber(@PathVariable long listId){



        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/id/{listId}/subscribe")
    public ResponseEntity<?> deleteSubscriber(@PathVariable long listId,@PathVariable long memberId){
        listsService.deleteMembers(listId,memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/subscribers")
    public ResponseEntity<List<ListsResponse>> getSubscribers(@PathVariable long id){

        return ResponseEntity.ok(listsService.getSubscribers(id));
    }
}
