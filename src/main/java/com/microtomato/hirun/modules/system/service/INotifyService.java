package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.Notify;

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
     * 删除公告
     *
     * @param idList
     */
    void deleteAnnounce(List<Long> idList);

    /**
     * 查未读公告
     *
     * @return
     */
    List<Notify> queryUnreadAnnounce();

    /**
     * 查未读公告
     *
     * @param userId
     * @return
     */
    List<Notify> queryUnreadAnnounce(Long userId);

    /**
     * 查未读私信
     *
     * @return
     */
    List<Notify> queryUnreadMessage();

    /**
     * 查未读私信
     *
     * @param userId 用户Id
     * @return
     */
    List<Notify> queryUnreadMessage(Long userId);

    /**
     * 发送私信
     *
     * @param toEmployeeId 目标雇员Id
     * @param content 私信内容
     */
    void sendMessage(Long toEmployeeId, String content);

    /**
     * 发送私信
     *
     * @param toEmployeeId 给谁发私信
     * @param content 私信内容
     * @param fromEmployeeId 谁发的私信
     */
    void sendMessage(Long toEmployeeId, String content, Long fromEmployeeId);

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

