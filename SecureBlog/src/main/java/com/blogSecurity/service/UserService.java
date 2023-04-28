package com.blogSecurity.service;

import com.blogSecurity.dto.request.Login;
import com.blogSecurity.dto.request.SignUp;
import com.blogSecurity.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(SignUp signUp);

    String loginUser(Login login);
}
