package com.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class ImageUploadController {

    private final WebApplicationContext context;

    @PostMapping(value = "/image-upload")
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        try {
            String uploadDirectory = Objects.requireNonNull(context.getServletContext()).getRealPath("/uploadImages");
            String originalFileName = file.getOriginalFilename();
            String fileExtension = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;
            file.transferTo(new File(uploadDirectory, uuidFileName));
            System.out.println("************************ 업로드 컨트롤러 실행 ************************");
            System.out.println(uploadDirectory);
            return ResponseEntity.ok(uuidFileName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이미지 업로드 실패");
        }
    }
}
