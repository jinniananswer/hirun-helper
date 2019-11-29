package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.entity.dto.UnReadedDTO;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.mapper.NotifyQueueMapper;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户消息队列 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Slf4j
@Service
public class NotifyQueueServiceImpl extends ServiceImpl<NotifyQueueMapper, NotifyQueue> implements INotifyQueueService {

    @Autowired
    private NotifyQueueMapper notifyQueueMapper;

    @Autowired
    private INotifyQueueService notifyQueueServiceImpl;

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private INotifyService notifyServiceImpl;

    /**
     * 从队列里获取当前用户最新数据的时间
     *
     * @return
     */
    @Override
    public LocalDateTime getLatestTime() {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        return getLatestTime(employeeId);
    }

    /**
     * 从队列里获取当前用户最新数据的时间
     *
     * @param employeeId 用户 Id
     * @return
     */
    @Override
    public LocalDateTime getLatestTime(Long employeeId) {
        NotifyQueue notifyQueue = notifyQueueMapper.selectOne(
            Wrappers.<NotifyQueue>lambdaQuery()
                .eq(NotifyQueue::getEmployeeId, employeeId)
                .orderByDesc(NotifyQueue::getCreateTime).last(" limit 1")
        );
        if (null == notifyQueue) {
            return LocalDateTime.now();
        } else {
            return notifyQueue.getCreateTime();
        }
    }

    /**
     * 公告的入队操作
     */
    @Override
    public void announceEnqueue() {
        List<Notify> list = notifyServiceImpl.queryUnreadAnnounce();
        enqueue(list);
    }

    /**
     * 消息的入队操作
     */
    @Override
    public void messageEnqueue() {
        List<Notify> list = notifyServiceImpl.queryUnreadMessage();
        enqueue(list);
    }

    private void enqueue(List<Notify> list) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDateTime now = LocalDateTime.now();

        for (Notify notify : list) {
            NotifyQueue notifyQueue = new NotifyQueue();
            notifyQueue.setNotifyId(notify.getId());
            notifyQueue.setEmployeeId(employeeId);
            notifyQueue.setReaded(false);
            notifyQueue.setCreateTime(now);
            notifyQueueServiceImpl.save(notifyQueue);
        }
    }

    /**
     * 查用户的未读消息（包括：公告/私信/提醒）
     *
     * @return 未读消息列表
     */
    @Override
    public List<UnReadedDTO> queryUnreadMessage() {
        return queryUnread(INotifyService.NotifyType.MESSAGE);
    }



    /**
     * 查未读公告
     *
     * @return
     */
    @Override
    public List<UnReadedDTO> queryUnreadAnnounce() {
        return queryUnread(INotifyService.NotifyType.ANNOUNCE);
    }

    private List<UnReadedDTO> queryUnread(INotifyService.NotifyType notifyType) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        List<UnReadedDTO> unReadedDTOS = notifyQueueMapper.queryAll(notifyType.value(), employeeId);

        Set<Long> ids = new HashSet<>();
        unReadedDTOS.forEach(unReadedDTO -> ids.add(unReadedDTO.getSenderId()));

        if (ids.size() > 0) {
            List<Employee> list = employeeServiceImpl.list(
                Wrappers.<Employee>lambdaQuery()
                    .select(Employee::getEmployeeId, Employee::getName)
                    .in(Employee::getEmployeeId, ids)
            );

            Map<Long, String> idNameMap = new HashMap<>(10);
            list.forEach(employee -> idNameMap.put(employee.getEmployeeId(), employee.getName()));
            unReadedDTOS.forEach(unreadedDTO -> unreadedDTO.setName(idNameMap.get(unreadedDTO.getSenderId())));
        }
        return unReadedDTOS;
    }

    /**
     * 标记消息已读
     *
     * @param notifyId 消息Id
     */
    @Override
    public void markReaded(Long notifyId) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        markReaded(notifyId, employeeId);
    }

    /**
     * 标记消息已读
     *
     * @param idList
     */
    @Override
    public void markReaded(List<Long> idList) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);
        notifyQueueMapper.update(entity, Wrappers.<NotifyQueue>lambdaUpdate()
            .eq(NotifyQueue::getEmployeeId, employeeId)
            .in(NotifyQueue::getNotifyId, idList)
        );

    }

    /**
     * 标记消息已读
     *
     * @param notifyId   消息Id
     * @param employeeId 雇员Id
     */
    @Override
    public void markReaded(Long notifyId, Long employeeId) {
        LambdaQueryWrapper<NotifyQueue> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifyQueue::getNotifyId, notifyId);
        lambdaQueryWrapper.eq(NotifyQueue::getEmployeeId, employeeId);

        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);
        notifyQueueMapper.update(entity, lambdaQueryWrapper);
    }

    /**
     * 标记全部已读
     */
    @Override
    public void markReadedAll(INotifyService.NotifyType notifyType) {

        List<UnReadedDTO> unReadedDTOS = queryUnread(notifyType);
        Set<Long> set = new HashSet<>();
        unReadedDTOS.forEach(unReadedDTO -> set.add(unReadedDTO.getId()));

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);

        notifyQueueMapper.update(entity,
            Wrappers.<NotifyQueue>lambdaUpdate()
                .eq(NotifyQueue::getEmployeeId, employeeId)
                .in(NotifyQueue::getNotifyId, set)
        );
    }

}
