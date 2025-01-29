package com.comitialsign.api.dtos.user;

import com.comitialsign.api.domain.user.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {
}
