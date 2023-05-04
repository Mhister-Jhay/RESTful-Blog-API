package com.blogSecurity.authApp.domain.security;

import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.authApp.domain.model.Roles;
import com.blogSecurity.authApp.domain.model.User;
import com.blogSecurity.authApp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new ResourceNotFoundException("User with username ("+username+") does not exist"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),mapRolesToAuthorities(user.getRole()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Roles role){
        return Collections.singleton(new SimpleGrantedAuthority(role.getName()));
    }

}
