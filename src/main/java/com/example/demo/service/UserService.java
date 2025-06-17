package com.example.demo.service;

import com.example.demo.entity.Account;

public interface UserService {
    int register(Account user);

    Account SelectUserById(Integer id);
}
