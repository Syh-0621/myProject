package com.example.demo.service.impl;

import com.example.demo.entity.ChatMsgVO;
import com.example.demo.mapper.ChatMsgMapper;
import com.example.demo.service.ChatMsgService;
import com.example.demo.util.fileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private fileUtil fileUtil;

    @Override
    public int insertChatMsg(ChatMsgVO chatMsg) {
        return chatMsgMapper.insert(chatMsg);
    }

    @Override
    public List<ChatMsgVO> selectChatMsgByUser(String MFromUser, String MToUser) {
        return chatMsgMapper.selectByUser(MFromUser, MToUser);
    }

    @Override
    public int updateChatMsg(ChatMsgVO chatMsg){
        return chatMsgMapper.update(chatMsg);
    }

    @Override
    public Integer getUnreadCount(String MFromUser, String MToUser) {
        Integer unreadCount = 0;
        for (ChatMsgVO chatMsgVO : selectChatMsgByUser(MFromUser, MToUser)) {
            if(!chatMsgVO.getIsRead() && chatMsgVO.getMFromUser().equals(MToUser)){
                unreadCount++;
            }
        }
        return unreadCount;
    }

    @Override
    public int deleteAllChatMsgByPid(Integer pid) {
        List<ChatMsgVO> chatMsgVOList = chatMsgMapper.selectMsgByPid(pid);
        for (ChatMsgVO chatMsgVO : chatMsgVOList) {
            if (chatMsgVO.getIsImg()){
                fileUtil.deleteFile(chatMsgVO.getMContent());
            }
            chatMsgMapper.deleteByMsg(chatMsgVO);
        }
        return 1;
    }
}
