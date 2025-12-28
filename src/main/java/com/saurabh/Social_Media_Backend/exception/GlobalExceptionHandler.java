package com.saurabh.Social_Media_Backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelGeneral(Exception ex,HttpServletRequest request){
        ErrorResponse response=new ErrorResponse(
                500,
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handelException(AppException e, HttpServletRequest request){
        ErrorResponse res=new ErrorResponse(
                e.getStatusCode(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(e.getStatusCode()).body(res);
    }

}
