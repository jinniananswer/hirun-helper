package com.microtomato.hirun.framework.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息推送服务端（WebSocket）
 *
 * @author Steven
 * @date 2019-11-02
 */
@ServerEndpoint("/websocket/{id}/{name}")
@Slf4j
@RestController
public class WebSocketServer {

    /**
     * 用来记录当前连接数的变量
     */
    private static volatile AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * concurrent 包的线程安全 Set，用来存放每个客户端对应的 WebSocketServer 对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来与客户端进行数据收发
     */
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") long id, @PathParam("name") String name) throws Exception {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        int onlineCount = getOnlineCount();
        log.info(" Open a websocket. sessionId: {}, id: {}, name: {}, onlineCount: {}", this.session.getId(), id, name, onlineCount);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Receive a message from client: " + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. ", error);
    }

    @OnClose
    public void onClose() {
        log.info("Close a websocket. sessionId: {}", this.session.getId());
        webSocketSet.remove(this);
        subOnlineCount();
    }

    /**
     * 发送消息
     */
    public void sendMessage(String message) throws Exception {
        if (this.session.isOpen()) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 广播消息
     */
    public void broadcast(String message) throws Exception {
        for (WebSocketServer webSocketServer : webSocketSet) {
            webSocketServer.sendMessage(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }
}
