package com.saurabh.Social_Media_Backend.service;

import com.saurabh.Social_Media_Backend.exception.AppException;
import com.saurabh.Social_Media_Backend.models.Blocked;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.BlockRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class checkBlockedService {
    private final BlockRepo blockRepo;
    private final getCurrentUserService getCurrentUserService;

    public checkBlockedService(BlockRepo blockRepo, getCurrentUserService getCurrentUserService) {
        this.blockRepo = blockRepo;
        this.getCurrentUserService = getCurrentUserService;
    }

    public Optional<Blocked> getBlock(Users users1){
       Users currentUser=getCurrentUserService.getCurrentUser();
       return blockRepo.findBlockedByBlockedIdAndBlockerId(currentUser,users1);
    }
}
