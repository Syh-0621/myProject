package com.example.demo.util;

import org.springframework.web.multipart.MultipartFile;

public interface fileUtil {
    String uploadFile(MultipartFile[] files, String username);
}
