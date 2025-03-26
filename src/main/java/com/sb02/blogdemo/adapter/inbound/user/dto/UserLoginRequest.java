package com.sb02.blogdemo.adapter.inbound.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank String id,
        @NotBlank String password
) {
}
