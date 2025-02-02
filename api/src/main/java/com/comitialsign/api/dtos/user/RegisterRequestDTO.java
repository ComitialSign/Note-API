package com.comitialsign.api.dtos.user;

import com.comitialsign.api.domain.user.UserRole;

public record RegisterRequestDTO(String username, String password, String confirmPassword, UserRole role) {
}
