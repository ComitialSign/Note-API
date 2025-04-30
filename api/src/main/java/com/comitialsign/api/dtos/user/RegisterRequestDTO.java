package com.comitialsign.api.dtos.user;

public record RegisterRequestDTO(String username, String password, String confirmPassword) {
}
