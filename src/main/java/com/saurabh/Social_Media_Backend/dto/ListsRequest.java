package com.saurabh.Social_Media_Backend.dto;

public record ListsRequest(
        String listName,
        String description,
        boolean isPrivate
){

}
