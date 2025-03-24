package com.sb02.blogdemo.user.domain.usecase;

public interface FindUserUseCase {
    boolean existsById(String userId);
}
