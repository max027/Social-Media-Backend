package com.saurabh.Social_Media_Backend.dto;

import com.saurabh.Social_Media_Backend.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    @Mapping(source = "userId", target = "userId")
    UserResponse toUserResponse(Users users);
}
