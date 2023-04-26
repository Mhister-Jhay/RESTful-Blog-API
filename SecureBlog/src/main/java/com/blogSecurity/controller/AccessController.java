package com.blogSecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessController {

    @GetMapping("/")
    public ResponseEntity<String> access(){
        return new ResponseEntity<>("You have been granted access", HttpStatus.OK);
    }
}
