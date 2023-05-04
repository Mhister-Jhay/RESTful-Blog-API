package com.blogSecurity.authApp.application.controller;

import com.blogSecurity.authApp.application.model.Login;
import com.blogSecurity.authApp.application.model.SignUp;
import com.blogSecurity.authApp.application.model.UserResponse;
import com.blogSecurity.authApp.domain.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody SignUp signUp){
        return new ResponseEntity<>(userServiceImpl.registerUser(signUp), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> loginUser(@Valid @RequestBody Login login){
        return new ResponseEntity<>(userServiceImpl.loginUser(login), HttpStatus.OK);
    }
}
