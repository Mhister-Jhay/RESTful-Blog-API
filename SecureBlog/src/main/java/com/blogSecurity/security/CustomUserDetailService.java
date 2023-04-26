package com.blogSecurity.security;

import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.model.Roles;
import com.blogSecurity.model.Users;
import com.blogSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username).orElseThrow(()->
                new ResourceNotFoundException("User with username ("+username+") does not exist"));
        return new User(users.getUsername(),users.getPassword(),mapRolesToAuthorities(users.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Roles> roles){
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
