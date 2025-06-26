package com.example.demo.service.impl;

import com.example.demo.util.fileUtil;
import com.example.demo.entity.Chat;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private fileUtil fileUtil;

    @Override
    public List<Chat> showAllChat(String username) {
        return chatMapper.selectBySender(username);
    }

    @Override
    public Chat selectByChat(Chat chat){
        return chatMapper.selectByChat(chat);
    }

    @Override
    public int addChat(Chat chat){
        return chatMapper.add(chat);
    }

    @Override
    public String uploadImg(MultipartFile[] images, String username) {
//        // 判断上传的文件是否为空
//        if (images.isEmpty())
//            return null;
//        // 定义文件保存路径
//        String folderpath = "/home/syh/Pictures/img/";
//        // 定义文件名
//        String filepath = username + "/" + new Random().nextInt(1000) + images.getOriginalFilename();
//        // 创建文件对象
//        File wholepath = new File(folderpath + filepath);
//        // 判断文件父目录是否存在，不存在则创建
//        if (!wholepath.getParentFile().exists()) {
//            wholepath.getParentFile().mkdirs();
//        }
//        try {
//            // 将上传的文件保存到指定路径
//            images.transferTo(wholepath);
//        } catch (IOException | IllegalStateException e) {
//            // 打印异常信息
//            e.printStackTrace();
//            // 抛出运行时异常
//            throw new RuntimeException(e);
//        }
//        return "/images/" + filepath;
        return fileUtil.uploadFile(images, username);
    }

    @Override
    public int deleteChatByPid(Integer pid){
        return chatMapper.deleteByPid(pid);
    }
}
