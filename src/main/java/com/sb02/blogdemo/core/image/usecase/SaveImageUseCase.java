package com.sb02.blogdemo.core.image.usecase;

import com.sb02.blogdemo.core.image.usecase.dto.SaveImageCommand;
import com.sb02.blogdemo.core.image.usecase.dto.SaveImageResult;

public interface SaveImageUseCase {
    SaveImageResult saveImage(SaveImageCommand command);
}
