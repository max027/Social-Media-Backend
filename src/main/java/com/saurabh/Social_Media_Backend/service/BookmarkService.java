package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.dto.BookmarkResponse;
import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Bookmarks;
import com.saurabh.Social_Media_Backend.models.Tweets;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.BookmarkRepo;
import com.saurabh.Social_Media_Backend.repo.TweetsRepo;
import com.saurabh.Social_Media_Backend.repo.UserRepo;
import com.saurabh.Social_Media_Backend.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookmarkService {
    private final BookmarkRepo bookmarkRepo;
    private final TweetsRepo tweetsRepo;
    private final UserRepo userRepo;

    public BookmarkService(BookmarkRepo repo,TweetsRepo tweetsRepo,UserRepo userRepo){
        this.bookmarkRepo=repo;
        this.tweetsRepo=tweetsRepo;
        this.userRepo=userRepo;
    }

    private Tweets checkIfExist(long id){
        Optional<Tweets> tweets=tweetsRepo.findById(id);
        if (tweets.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND.value(), "Tweet with id:"+id+" not found");
        }
        return tweets.get();
    }

    private Users checkUser(){
        return userRepo.findByEmail(SecurityUtils.getCurrentUser()).orElseThrow(
                ()-> new UsernameNotFoundException("user not found")
        );
    }
    public BookmarkResponse createBookmark(long id){
        Tweets tweets=checkIfExist(id);
        Users users=checkUser();
        Bookmarks bookmarks=new Bookmarks();

        //inc count
        tweets.setBookmarkCount(tweets.getBookmarkCount()+1);

        bookmarks.setUsers(users);
        bookmarks.setTweetsId(tweets);
        bookmarkRepo.save(bookmarks);

        return new BookmarkResponse(tweets.getUsers().getUserId(), tweets.getTweetsId());
    }

    public void deleteBookmark(long id){
        Tweets tweets=checkIfExist(id);
        Users users=checkUser();

        Bookmarks bookmarks=bookmarkRepo.findBookmarksByUsersAndTweetsId(users,tweets).orElseThrow(
                ()->new AppException(HttpStatus.NOT_FOUND.value(), "user does not have bookmark")
        );

        tweets.setBookmarkCount(tweets.getBookmarkCount()-1);
        bookmarkRepo.deleteById(bookmarks.getBookmarkId());
    }

    public List<BookmarkResponse> getAllBookmarks(){
       Users users=checkUser();
       List<Bookmarks>bookmarksList=bookmarkRepo.findBookmarksByUsers(users);
       List<BookmarkResponse>responses=new ArrayList<>();

       for (Bookmarks bookmarks:bookmarksList){
           BookmarkResponse r=new BookmarkResponse(
                   bookmarks.getUsers().getUserId(),
                   bookmarks.getTweetsId().getTweetsId()
           );
           responses.add(r);
       }
       return responses;
    }
}
