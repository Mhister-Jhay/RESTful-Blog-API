package com.blogSecurity.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> restricted(RestrictedAccessException e,
                                                          HttpServletRequest request){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.FORBIDDEN.value())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> approval(ResourceApprovalException e,
                                                        HttpServletRequest request){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.PROCESSING.value())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.PROCESSING);
    }
    @ExceptionHandler
    public ResponseEntity<Object> invalid(MethodArgumentNotValidException e,
                                          HttpServletRequest request){
        Map<String, String> invalidErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error->{
            String fieldError = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            invalidErrors.put(fieldError,message);
        });
        invalidErrors.put("errorTime", LocalDateTime.now().toString());
        invalidErrors.put("path",request.getRequestURI());
        return new ResponseEntity<>(invalidErrors, HttpStatus.BAD_REQUEST);
    }
}
