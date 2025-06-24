package com.blog.backend.component;

import com.alibaba.fastjson.JSONObject;
import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    private Session session;

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        System.out.println("connected!");
    }

    @OnClose
    public void onClose() {
        System.out.println("disconnected!");

    }

    @OnMessage
    public void onMessage(String message, Session session) {
//        System.out.println("receive message!");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            System.out.println("start-matching");
//            startMathing();
        }else if ("stop-matching".equals(event)) {
            System.out.println("stop-matching");
//            stopMatching();
        }
    }

    @OnError
    public void onError(Session session,Throwable throwable) {
        throwable.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
