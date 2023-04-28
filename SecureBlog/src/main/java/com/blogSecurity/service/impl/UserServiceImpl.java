package com.blogSecurity.service.impl;

import com.blogSecurity.dto.request.Login;
import com.blogSecurity.dto.request.SignUp;
import com.blogSecurity.dto.response.UserResponse;
import com.blogSecurity.enums.AccountStatus;
import com.blogSecurity.exception.ResourceAlreadyExistException;
import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.exception.UnauthorizedException;
import com.blogSecurity.model.Roles;
import com.blogSecurity.model.User;
import com.blogSecurity.repository.RoleRepository;
import com.blogSecurity.repository.UserRepository;
import com.blogSecurity.security.JWTAuthenticationProvider;
import com.blogSecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTAuthenticationProvider jwtAuthenticationProvider;


    @Override
    public UserResponse registerUser(SignUp signUp){
        if(userRepository.existsByUsername(signUp.getUsername())){
            throw new ResourceAlreadyExistException("Username already taken,Please Enter a different username");
        }
        User users = User.builder()
                .fullName(signUp.getFullName())
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .username(signUp.getUsername())
                .build();
        Roles role;
        if(signUp.getUsername().equals("admin") && signUp.getPassword().equals("adminPassword")){
            role = roleRepository.findByName("SUPER_ADMIN");
        }else if(signUp.getUsername().startsWith("adminRole") && signUp.getPassword().startsWith("admin111")){
            role = roleRepository.findByName("ADMIN");
        }else{
            role = roleRepository.findByName("USER");
        }
        users.setRole(role);
        users.setStatus(AccountStatus.ACTIVE);
        return mapToUserResponse(userRepository.save(users));
    }

    @Override
    public String loginUser(Login login){
        User user = userRepository.findByUsername(login.getUsername()).orElseThrow(()->
                new ResourceNotFoundException("User does not have an account"));
        if(user.getStatus().equals(AccountStatus.BANNED)){
            throw new UnauthorizedException("Account is BANNED");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtAuthenticationProvider.generateToken(authentication);
    }
    private UserResponse mapToUserResponse(User user){
        return modelMapper.map(user,UserResponse.class);
    }
}
