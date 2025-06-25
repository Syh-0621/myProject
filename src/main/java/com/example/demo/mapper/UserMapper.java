package com.example.demo.mapper;

import com.example.demo.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    Account selectUser(String username);

    @Insert("insert into user(username, password, role, email, nickname, profilePicture) values(#{username}, #{password}, 'user', #{email}, #{nickname}, #{profilePicture})")
    int insertUser(Account account);

    @Select("select * from user where id=#{id}")
    Account SelectUserById(Integer id);

    @Update("update user set username=#{username}, password=#{password}, email=#{email}, nickname=#{nickname}, profilePicture=#{profilePicture} where id=#{id}")
    int UpdateUser(Account user);
}
