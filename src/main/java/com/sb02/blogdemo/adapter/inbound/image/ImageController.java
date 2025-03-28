package com.sb02.blogdemo.adapter.inbound.image;

import com.sb02.blogdemo.auth.RequiresAuth;
import com.sb02.blogdemo.core.image.usecase.SaveImageUseCase;
import com.sb02.blogdemo.core.image.usecase.dto.SaveImageCommand;
import com.sb02.blogdemo.core.image.usecase.dto.SaveImageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final SaveImageUseCase saveImageUseCase;

    @PostMapping
    @RequiresAuth
    public ResponseEntity<UploadImageResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        SaveImageCommand command = new SaveImageCommand(file);
        SaveImageResult result = saveImageUseCase.saveImage(command);

        return ResponseEntity.ok(new UploadImageResponse(true, result.imageId().toString(), result.uploadPath()));
    }
}
