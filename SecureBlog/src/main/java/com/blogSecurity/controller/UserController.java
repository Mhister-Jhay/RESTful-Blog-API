package com.blogSecurity.controller;

import com.blogSecurity.dto.Login;
import com.blogSecurity.dto.SignUp;
import com.blogSecurity.service.impl.UserServiceImpl;
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
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/sign-up")
    public ResponseEntity<String> registerUser(@RequestBody SignUp signUp){
        return new ResponseEntity<>(userServiceImpl.registerUser(signUp), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> loginUser(@RequestBody Login login){
        log.info("Login details == {}",login);
        return new ResponseEntity<>(userServiceImpl.loginUser(login), HttpStatus.OK);
    }
}
