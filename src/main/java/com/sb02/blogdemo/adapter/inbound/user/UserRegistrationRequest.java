package com.sb02.blogdemo.adapter.inbound.user;

import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String id,
        @NotBlank String password,
        @NotBlank String email,
        @NotBlank String nickname
) {
}
