package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.BookmarkResponse;
import com.saurabh.Social_Media_Backend.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService service;

    public BookmarkController(BookmarkService service){
        this.service=service;
    }

    @PostMapping("/id/{id}")
    public ResponseEntity<BookmarkResponse>bookmark(@PathVariable long id){
        BookmarkResponse response=service.createBookmark(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?>deleteBookmark(@PathVariable long id){
        service.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getBookmark(){
        return ResponseEntity.ok(service.getAllBookmarks());
    }
}
