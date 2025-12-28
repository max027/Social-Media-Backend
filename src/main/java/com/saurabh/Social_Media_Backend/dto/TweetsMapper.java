package com.saurabh.Social_Media_Backend.dto;

import com.saurabh.Social_Media_Backend.models.Tweets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TweetsMapper {
    @Mapping(source = "tweetsId", target = "id")
    @Mapping(source = "users.userId", target = "userId")
    TweetsResponse toResponse(Tweets tweets);
}
