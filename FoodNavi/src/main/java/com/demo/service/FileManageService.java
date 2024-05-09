package com.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileManageService {
    String uploadFile(MultipartFile file);
}
