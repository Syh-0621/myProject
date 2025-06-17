package com.example.demo.mapper;

import com.example.demo.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    Account selectUser(String username);

    @Insert("insert into user(username, password, role, email) values(#{username}, #{password}, 'user', #{email})")
    int insertUser(Account account);

    @Select("select * from user where id=#{id}")
    Account SelectUserById(Integer id);
}
