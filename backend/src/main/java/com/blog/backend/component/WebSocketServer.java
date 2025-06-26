package com.blog.backend.component;

import com.alibaba.fastjson.JSONObject;
import com.blog.backend.component.utils.JwtAuthentication;
import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    final public static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

    private User user;
    private Session session;

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("connected!");
        System.out.println("DEBUG: Token received in onOpen: [" + token + "]");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null){
            users.put(userId, this);
        }else {
            this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        System.out.println("disconnected!");
        if (this.user != null){
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    private void startMatching(){
        System.out.println("startMatching");
        matchpool.add(user);

        while(matchpool.size()>=2){
            Iterator<User> iterator = matchpool.iterator();
            User a = iterator.next();
            User b = iterator.next();
            matchpool.remove(a);
            matchpool.remove(b);

            JSONObject responseA = new JSONObject();
            responseA.put("event", "start-game");
            responseA.put("opponent_username", b.getUsername());
            responseA.put("opponent_photo", b.getPhoto());
            users.get(a.getId()).sendMessage(responseA.toJSONString());

            JSONObject responseB = new JSONObject();
            responseB.put("event", "start-game");
            responseB.put("opponent_username", a.getUsername());
            responseB.put("opponent_photo", a.getPhoto());
            System.out.println(responseB);
            users.get(b.getId()).sendMessage(responseB.toJSONString());
        }
    }

    private void stopMatching(){
        System.out.println("stopMatching");
        matchpool.remove(user);
    }



    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("receive message!");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        }else if ("stop-matching".equals(event)) {
            System.out.println("stop-matching");
            stopMatching();
        }else if ("move".equals(event)) {
//            move(data.getInteger("direction"));
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
