package com.blogSecurity.service.impl;

import com.blogSecurity.dto.Login;
import com.blogSecurity.dto.SignUp;
import com.blogSecurity.exception.ResourceAlreadyExistException;
import com.blogSecurity.model.Roles;
import com.blogSecurity.model.Users;
import com.blogSecurity.repository.RoleRepository;
import com.blogSecurity.repository.UserRepository;
import com.blogSecurity.security.CustomUserDetailService;
import com.blogSecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CustomUserDetailService customUserDetailService;
    private final DaoAuthenticationProvider daoAuthenticationProvider;


    @Override
    public String registerUser(SignUp signUp){
        if(userRepository.existsByUsername(signUp.getUsername())){
            throw new ResourceAlreadyExistException("Username already taken,Please Enter a different username");
        }
        Set<Roles> roles = new HashSet<>();
        if(signUp.getUsername().equals("admin") && signUp.getPassword().equals("adminPassword")){
            Roles roles1 = roleRepository.findByName("ADMIN");
            Roles roles2 = roleRepository.findByName("USER");
            roles.add(roles1);
            roles.add(roles2);
        }else{
            Roles role = roleRepository.findByName("USER");
            roles.add(role);
        }
        Users users = Users.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .username(signUp.getUsername())
                .build();
        users.setRoles(roles);
        userRepository.save(users);
        return "User Registered Successfully";
    }

    @Override
    public String loginUser(Login login){
        UserDetails userDetails = customUserDetailService.loadUserByUsername(login.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                login.getUsername(),login.getPassword());
        authentication = daoAuthenticationProvider.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "User Login Successful";
    }
}
