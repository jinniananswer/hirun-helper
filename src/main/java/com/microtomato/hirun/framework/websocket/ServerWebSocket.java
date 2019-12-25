package com.microtomato.hirun.framework.websocket;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.util.JsonUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.entity.dto.UnReadedDTO;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息推送服务端（WebSocket）
 *
 * @author Steven
 * @date 2019-11-02
 */
@ServerEndpoint(value = "/websocket/{id}")
@Slf4j
@RestController
public class ServerWebSocket {

    private static final Map<Long, List<ServerWebSocket>> webSocketMap = new ConcurrentHashMap<>();

    private Long id;

    /**
     * 与某个客户端的连接会话，需要通过它来与客户端进行数据收发
     */
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) {
        this.session = session;
        this.id = id;

        if (!webSocketMap.containsKey(this.id)) {
            webSocketMap.put(this.id, new ArrayList<ServerWebSocket>());
        }

        webSocketMap.get(this.id).add(this);
        log.info(" Open a websocket. sessionId: {}, employeeId: {}", this.session.getId(), this.id);
    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. ", error);
    }

    @OnClose
    public void onClose() {
        log.info("Close a websocket. sessionId: {}", this.session.getId());
        webSocketMap.get(this.id).remove(this);
    }

    /**
     * 发送消息
     */
    private void sendMessage(String message) {
        if (this.session.isOpen()) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息
     *
     * @param toId    发给谁
     * @param notify 消息
     */
    public static void sendMessage(Long toId, Notify notify) {

        String message = convert(notify);

        if (webSocketMap.containsKey(toId)) {
            List<ServerWebSocket> webSockets = webSocketMap.get(toId);
            for (ServerWebSocket webSocket : webSockets) {
                webSocket.sendMessage(message);
            }
        }
    }

    /**
     * 转换
     *
     * @param notify
     * @return
     */
    private static String convert(Notify notify) {
        IEmployeeService employeeServiceImpl = SpringContextUtils.getBean(IEmployeeService.class);

        UnReadedDTO unReadedDTO = new UnReadedDTO();
        BeanUtils.copyProperties(notify, unReadedDTO);
        Employee one = employeeServiceImpl.getOne(
            Wrappers.<Employee>lambdaQuery()
                .select(Employee::getName)
                .eq(Employee::getEmployeeId, notify.getSenderId())
        );
        unReadedDTO.setName(one.getName());

        Integer notifyType = notify.getNotifyType();
        if (INotifyService.NotifyType.ANNOUNCE.value() == notifyType) {
            unReadedDTO.setNotifyTypeDesc("公告");
        } else if(INotifyService.NotifyType.NOTICE.value() == notifyType) {
            unReadedDTO.setNotifyTypeDesc("通知");
        } else if(INotifyService.NotifyType.MESSAGE.value() == notifyType) {
            unReadedDTO.setNotifyTypeDesc("私信");
        }

        return JsonUtils.encode(unReadedDTO);
    }

    /**
     * 广播消息
     *
     * @param notify 消息
     */
    public static void sendMessageBroadcast(Notify notify) {
        String message = convert(notify);

        for (Long id : webSocketMap.keySet()) {
            List<ServerWebSocket> sockets = webSocketMap.get(id);
            for (ServerWebSocket socket : sockets) {
                socket.sendMessage(message);
            }
        }
    }

    /**
     * 获取所有在线 Id 集合
     *
     * @return
     */
    public static Set<Long> onlineIds() {
        return webSocketMap.keySet();
    }

}