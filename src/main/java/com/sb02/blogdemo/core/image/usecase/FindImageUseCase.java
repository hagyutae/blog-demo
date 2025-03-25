package com.sb02.blogdemo.core.image.usecase;

import java.util.UUID;

public interface FindImageUseCase {
    boolean existsById(UUID imageId);
}
