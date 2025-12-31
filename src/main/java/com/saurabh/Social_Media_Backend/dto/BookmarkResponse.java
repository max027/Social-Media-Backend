package com.saurabh.Social_Media_Backend.dto;

import lombok.Getter;
import lombok.Setter;

public record BookmarkResponse(
        long userId,
        long tweetsId
){}
