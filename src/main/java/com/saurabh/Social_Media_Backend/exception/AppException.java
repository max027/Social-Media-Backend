package com.saurabh.Social_Media_Backend.exception;

public class AppException extends RuntimeException{
    private final int statusCode;
    public AppException(int statusCode,String msg){
        super(msg);
        this.statusCode=statusCode;
    }

    public AppException(int statusCode,String msg,Throwable t){
        super(msg,t);
        this.statusCode=statusCode;
    }

    public int getStatusCode(){
        return statusCode;
    }

}
