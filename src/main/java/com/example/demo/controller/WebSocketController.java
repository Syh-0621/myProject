package com.example.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.entity.ChatMsgVO;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{user}")
public class WebSocketController {

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
        JSONObject jsonObject = JSONObject.parseObject(message);
        sendToUser(jsonObject.toJavaObject(ChatMsgVO.class));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendToUser(ChatMsgVO chatMsg) {
        String toUser = chatMsg.getMToUser();
        String fromUser = chatMsg.getMFromUser();
        String mMsg = chatMsg.getMContent();
        System.out.println(fromUser + "发送消息：" + mMsg + "给" + chatMsg.getMToUser());
        System.out.println(webSocketMap);
        try {
            if (webSocketMap.containsKey(fromUser) && webSocketMap.containsKey(toUser)) {
                webSocketMap.get(fromUser).sendMessage(JSON.toJSONString(chatMsg));
                webSocketMap.get(toUser).sendMessage(JSON.toJSONString(chatMsg));
            }
            else {
                webSocketMap.get(fromUser).sendMessage(JSON.toJSONString(chatMsg));
                webSocketMap.get(fromUser).sendMessage(JSON.toJSONString(new ChatMsgVO("server", fromUser, "对方不在线")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
