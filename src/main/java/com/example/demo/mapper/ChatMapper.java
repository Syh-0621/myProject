package com.example.demo.mapper;

import com.example.demo.entity.Chat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatMapper {

    @Select("SELECT * FROM chat WHERE user1 = #{username} OR user2 = #{username}")
    List<Chat> selectBySender(String username);

    @Select("SELECT * FROM chat WHERE user1 = #{user1} AND user2 = #{user2} AND pid = #{pid} OR user1 = #{user2} AND user2 = #{user1} AND pid = #{pid}")
    Chat selectByChat(Chat chat);

    @Insert("INSERT INTO chat(user1, user2, pid) VALUES(#{user1}, #{user2}, #{pid})")
    int add(Chat chat);
}
