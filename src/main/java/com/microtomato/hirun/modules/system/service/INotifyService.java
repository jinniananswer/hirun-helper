package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
public interface INotifyService extends IService<Notify> {

    /**
     * 发送公告
     *
     * @param content 公告内容
     */
    void sendAnnounce(String content);

    /**
     * 发送提醒
     *
     * @param content    提醒内容，可以为空！
     * @param targetId
     * @param targetType
     * @param action
     */
    void sendRemind(String content, long targetId, TargetType targetType, Action action);

    /**
     * 根据订阅信息，查询消息数据
     *
     * @param notifySubscribe 订阅信息
     * @param createTime      时间
     * @return 消息
     */
    List<Notify> queryNotifyByNotifySubscribe(NotifySubscribe notifySubscribe, LocalDateTime createTime);

    /**
     * 消息类型
     */
    enum NotifyType {

        // 公告
        ANNOUNCE(1),

        // 提醒
        REMIND(2),

        // 私信
        MESSAGE(3);

        private int value = 0;

        NotifyType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    /**
     * 目标类型
     */
    enum TargetType {

        // 合同
        CONTRACT("contract"),

        // 方案
        PLAN("plan"),

        // 预算
        BUDGET("budget"),

        // 申请
        APPLICATION("application"),

        // 根据业务需求待补充...
        ;

        private String value = "";

        TargetType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * 提醒的动作类型
     */
    enum Action {

        // 关注
        FOLLOW("follow"),

        // 发布
        RELEASE("release"),

        // 更新
        UPDATE("update"),

        // 根据业务需求待补充...
        ;

        private String value = "";

        Action(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}

