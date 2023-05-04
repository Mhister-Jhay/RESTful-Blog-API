package com.blogSecurity.constant;

import com.blogSecurity.exception.UnauthorizedException;
import com.blogSecurity.authApp.domain.model.User;
import com.blogSecurity.authApp.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class UserDetails {
    private static UserRepository userRepository;

    @Autowired
    public UserDetails(UserRepository userRepository) {
        UserDetails.userRepository = userRepository;
    }

    public static User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated())
        {
            String username = authentication.getName();
            return userRepository.findByUsername(username).orElseThrow();
        } else {
            throw new UnauthorizedException("No authentication provided");
        }
    }
}
