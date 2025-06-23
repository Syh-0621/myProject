package com.example.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.entity.Chat;
import com.example.demo.entity.ChatMsgVO;
import com.example.demo.service.ChatMsgService;
import com.example.demo.service.ChatService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{user}")
public class WebSocketController {

    static ChatMsgService chatMsgService;

    @Autowired
    public void setChatMsgService(ChatMsgService chatMsgService) {
        WebSocketController.chatMsgService = chatMsgService;
    }

    static ChatService chatService;

    @Autowired
    public void setChatService(ChatService chatService) {
        WebSocketController.chatService = chatService;
    }

    private Session WebSocketSession;

    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, WebSocketController> webSocketMap = new ConcurrentHashMap<>();

    private String user;

    @OnOpen
    public void onOpen(@PathParam("user")String param, Session WebSocketSession) {
        this.user = param;
        this.WebSocketSession = WebSocketSession;
        webSocketMap.put(param, this);
        addOnlineCount();
    }

    @OnClose
    public void onClose() {
        if (!user.equals("")) {
            webSocketMap.remove(user);
            subOnlineCount();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        ChatMsgVO mes = JSONObject.parseObject(message, ChatMsgVO.class);
        System.out.println(mes);
        sendToUser(mes);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendToUser(ChatMsgVO chatMsg) {
        String toUser = chatMsg.getMToUser();
        String toUserAlert = toUser.substring(0, toUser.indexOf("@"));
        Integer pid = Integer.valueOf(toUser.substring(toUser.indexOf("@Pid=") + 5));
        String fromUser = chatMsg.getMFromUser();
        String mMsg = chatMsg.getMContent();

        System.out.println(fromUser + "发送消息：" + mMsg + "给" + chatMsg.getMToUser());
        System.out.println(webSocketMap);
        if (!chatMsg.getIsConfirmed()) {
            chatMsgService.insertChatMsg(chatMsg);
        } else {
            chatMsgService.updateChatMsg(chatMsg);
        }
        try {
            if (webSocketMap.containsKey(fromUser) && webSocketMap.containsKey(toUser)) {
                webSocketMap.get(toUser).sendMessage(JSON.toJSONString(chatMsg));
            }
            else {
                if (webSocketMap.containsKey(toUserAlert)){
                    webSocketMap.get(toUserAlert).sendMessage(JSON.toJSONString(new ChatMsgVO(fromUser, toUser, false, "", null,true,false)));
                }
                webSocketMap.get(fromUser).sendMessage(JSON.toJSONString(new ChatMsgVO("server", fromUser, false, "对方不在线","",false,false)));
            }
        } catch (Exception ignored) {
            // ignored
        }
    }

    public void sendMessage(String message) throws Exception {
        this.WebSocketSession.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }
}
