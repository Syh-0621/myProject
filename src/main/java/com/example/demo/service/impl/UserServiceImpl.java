package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.fileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private fileUtil fileUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userMapper.selectUser(username);
        log.info("用户信息：", user);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return User.withUsername(user.getUsername()).password(user.getPassword()).authorities(user.getRole()).build();
    }

    @Override
    public int register(Account user, MultipartFile[] profilePicture){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userMapper.selectUser(user.getUsername()) != null){
            return 0;
        }
        if (profilePicture != null) {
            String fileName = fileUtils.uploadFile(profilePicture, user.getUsername());
            user.setProfilePicture(fileName);
        }
        return userMapper.insertUser(user);
    }

    @Override
    public Account SelectUserById(Integer id){
        return userMapper.SelectUserById(id);
    }

    @Override
    public Account SelectUserByUsername(String username){
        return userMapper.selectUser(username);
    }

    @Override
    public int editUser(Account user, MultipartFile[] profilePicture){
        if (profilePicture != null) {
            String fileName = fileUtils.uploadFile(profilePicture, user.getUsername());
            user.setProfilePicture(fileName);
        } else {
            user.setProfilePicture(userMapper.SelectUserById(user.getId()).getProfilePicture());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.UpdateUser(user);
    }

}
