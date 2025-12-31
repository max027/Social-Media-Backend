package com.saurabh.Social_Media_Backend.dto;

import com.saurabh.Social_Media_Backend.models.Users;

public record ListsResponse(
        long listId,
        Users userId,
        String listName,
        String description,
        boolean isPrivate,
        int membersCount,
        int subscriberCount
) {
}
