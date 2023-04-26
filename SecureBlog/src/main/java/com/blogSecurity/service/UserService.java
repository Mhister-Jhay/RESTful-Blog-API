package com.blogSecurity.service;

import com.blogSecurity.dto.Login;
import com.blogSecurity.dto.SignUp;

public interface UserService {
    String registerUser(SignUp signUp);

    String loginUser(Login login);
}
