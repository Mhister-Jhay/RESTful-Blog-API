package com.blogSecurity.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private LocalDateTime errorTime;
    private String errorMessage;
    private String errorPath;
    private Integer errorStatusCode;
}
