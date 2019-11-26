package com.microtomato.hirun.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.system.entity.dto.AnnounceDTO;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户消息队列 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Storage
public interface NotifyQueueMapper extends BaseMapper<NotifyQueue> {

    @Select("select n.id, n.content, n.sender_id, n.create_time from sys_notify n, sys_notify_queue q where n.id = q.notify_id and n.notify_type = #{notifyType} and q.employee_id = #{employeeId}")
    List<AnnounceDTO> queryUnreadAnnounce(@Param("notifyType") Integer notifyType, @Param("employeeId") Long employeeId);

}
