package com.example.demo.mapper;

import com.example.demo.entity.ChatMsgVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatMsgMapper {
    @Select("SELECT * FROM chat_msg WHERE MFromUser = #{MFromUser} AND MToUser = #{MToUser} OR MFromUser = #{MToUser} AND MToUser = #{MFromUser} ORDER BY MTime ASC")
    List<ChatMsgVO> selectByUser(String MFromUser, String MToUser);

    @Insert("INSERT INTO chat_msg(MFromUser, MToUser, isImg, MContent, MTime, isRead, isConfirmed) VALUES(#{MFromUser}, #{MToUser}, #{isImg}, #{MContent}, #{MTime}, #{isRead}, #{isConfirmed})")
    int insert(ChatMsgVO chatMsg);

    @Select("SELECT * FROM chat_msg WHERE MFromUser = #{MFromUser} AND MToUser = #{MToUser} AND isRead = false")
    List<ChatMsgVO> selectUnread(String MFromUser, String MToUser);

    @Update("UPDATE chat_msg SET isRead = true WHERE MFromUser = #{MToUser} AND MToUser = #{MFromUser} AND isRead = false")
    int update(ChatMsgVO chatMsg);
}
