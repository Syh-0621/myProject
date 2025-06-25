package com.example.demo.util.impl;

import com.example.demo.util.fileUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class fileUtilImpl implements fileUtil {

    /**
     * 上传文件
     * @param files 需要上传的文件，类型为MultipartFile[]
     * @param username 上传文件的用户名
     * @return  返回文件路径，以逗号分隔
     */
    @Override
    public String uploadFile(MultipartFile[] files, String username) {
        if (files == null || files.length == 0)
            return null;
        String folderpath = "/home/syh/Pictures/img/";
        String[] suffixs = new String[]{"jpg", "png", "jpeg", "bmp", "gif"};
        List<String> filespath = new ArrayList<>();

        for (MultipartFile file : files){
            if (file.isEmpty())
                continue;
            if (file.getSize() > 1024 * 1024 * 5) {
                throw new RuntimeException("图片大小不能超过5MB");
            }
            String filepath = username + "/" + new Random().nextInt(1000) + file.getOriginalFilename();
            File wholepath = new File(folderpath + filepath);
            if (!wholepath.getParentFile().exists()) {
                wholepath.getParentFile().mkdirs();
            }
            try {
                file.transferTo(wholepath);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            filespath.add("/images/" + filepath);
        }
        return String.join(",", filespath);
    }
}
