package com.blogSecurity.dto.response;

import com.blogSecurity.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private RoleResponse role;
    private AccountStatus status;
}
