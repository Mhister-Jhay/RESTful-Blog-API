package com.blogSecurity.authApp.domain.service;

import com.blogSecurity.authApp.application.model.Login;
import com.blogSecurity.authApp.application.model.SignUp;
import com.blogSecurity.authApp.application.model.UserResponse;

public interface UserService {
    UserResponse registerUser(SignUp signUp);

    String loginUser(Login login);
}
