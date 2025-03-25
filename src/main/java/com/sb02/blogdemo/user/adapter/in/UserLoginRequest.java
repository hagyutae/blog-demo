package com.sb02.blogdemo.user.adapter.in;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank String id,
        @NotBlank String password
) {
}
