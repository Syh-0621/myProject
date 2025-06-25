package com.example.demo.service;

import com.example.demo.entity.Account;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    int register(Account user, MultipartFile[] profilePicture);

    Account SelectUserById(Integer id);

    Account SelectUserByUsername(String username);

    int editUser(Account user, MultipartFile[] profilePicture);
}
