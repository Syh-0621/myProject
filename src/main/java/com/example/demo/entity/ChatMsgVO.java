package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMsgVO {
    private String MFromUser;
    private String MToUser;
    private String MContent;
}
