package neko.transaction.member.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.ChatWebSocketMessageType;
import neko.transaction.commonbase.utils.entity.ProfilesActive;
import neko.transaction.member.vo.ChatWebSocketVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/chat/{token}")
@Slf4j
public class WebSocket {
    @Value("${spring.profiles.active}")
    private String active;

    private Boolean isDebug = true;

    @PostConstruct
    public void init(){
        if(ProfilesActive.PROP.equals(active)){
            isDebug = false;
        }
    }

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private String uid;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
     */
    private static final CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    /**
     * 存储在线连接用户信息
     */
    private static final ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            this.session = session;
            this.uid = StpUtil.getLoginIdByToken(token).toString();
            webSockets.add(this);
            sessionPool.put(this.uid, session);

            if(isDebug){
                log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.uid);

            if(isDebug){
                log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        if(message == null){
            return;
        }

        ChatWebSocketVo vo = JSONUtil.toBean(message, ChatWebSocketVo.class);
        if(ChatWebSocketMessageType.HEALTH_CHAT.equals(vo.getType())){
            return;
        }

        if(isDebug){
            log.info("【websocket消息】收到uid: " + this.uid + "，消息: " + vo);
        }
    }

    /** 发送错误时的处理
     */
    @OnError
    public void onError(Session session, Throwable e) {

        log.error("用户错误,原因:" + e.getMessage());

        e.printStackTrace();
    }


    /**
     * 广播消息
     * @param message 消息
     */
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for(WebSocket webSocket : webSockets) {
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息
     */
    public boolean sendOneMessage(String uid, String message) {
        Session session = sessionPool.get(uid);

        if (session != null&&session.isOpen()) {
            try {
                session.getAsyncRemote().sendText(message);
                if(isDebug){
                    log.info("【websocket消息】 单点消息:" + message);
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 单点消息(多人)
     */
    public void sendMoreMessage(String[] userIds, String message) {
        for(String userId : userIds) {
            Session session = sessionPool.get(userId);

            if (session != null && session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
