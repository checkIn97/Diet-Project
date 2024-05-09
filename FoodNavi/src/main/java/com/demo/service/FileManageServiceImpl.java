package com.demo.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileManageServiceImpl implements FileManageService {

    private static final String FILE_ROOT = "C:\\Diet-Project\\FoodNavi\\src\\main\\resources\\static\\uploadImages\\";

    @Override
    public String uploadFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + extension;
        File targetFile = new File(FILE_ROOT + savedFileName);

        try {
            InputStream fileStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            return "/uploadImages/" + savedFileName;
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);
            e.printStackTrace();
            return null;
        }
    }
}