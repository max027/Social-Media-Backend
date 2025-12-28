package com.saurabh.Social_Media_Backend.exception;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime time;
    private int status;
    private String error;
    private String path;

    public ErrorResponse(int status, String error, String path){
        this.time=LocalDateTime.now();
        this.status=status;
        this.error=error;
        this.path=path;
    }

}
