package com.blogSecurity.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> alreadyExist(ResourceAlreadyExistException e,
                                                          HttpServletRequest request){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.BAD_REQUEST.value())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> notFound(ResourceNotFoundException e,
                                                          HttpServletRequest request){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.NOT_FOUND.value())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> unauthorized(UnauthorizedException e,
                                                          HttpServletRequest request){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.UNAUTHORIZED.value())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.UNAUTHORIZED);
    }
}
