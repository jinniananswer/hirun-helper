package com.microtomato.hirun.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.entity.dto.UnReadedDTO;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.Local;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@RestController
@Slf4j
@RequestMapping("/api/system/notify")
public class NotifyController {

    @Autowired
    private INotifyService notifyServiceImpl;

    @PostMapping("/sendAnnounce")
    @RestResult
    public void sendAnnounce(@RequestBody String content) {
        content = StringEscapeUtils.unescapeJava(content);
        content = StringUtils.strip(content, "\"");
        notifyServiceImpl.sendAnnounce(content);
    }

    @PostMapping("deleteAnnounce")
    @RestResult
    public void deleteAnnounce(@RequestBody List<Long> idList) {
        notifyServiceImpl.deleteAnnounce(idList);
    }

    @GetMapping("/sendMessage")
    @RestResult
    public void sendMessage() {
        notifyServiceImpl.sendMessage(1473L, "这是一条测试消息");
    }

    /**
     * 查所有的公告信息
     *
     * @return
     */
    @GetMapping("announce-list-all")
    @RestResult
    public List<UnReadedDTO> announceListAll(@RequestParam(required = false) LocalDateTime startTime, @RequestParam(required = false) LocalDateTime endTime) {

        if (null == startTime) {
            startTime = LocalDateTime.now().minusDays(10);
        }

        if (null == endTime) {
            endTime = LocalDateTime.now();
        }

        List<Notify> list = notifyServiceImpl.list(
            Wrappers.<Notify>lambdaQuery()
                .select(Notify::getId, Notify::getContent, Notify::getSenderId, Notify::getCreateTime)
                .eq(Notify::getNotifyType, INotifyService.NotifyType.ANNOUNCE.value())
                .gt(Notify::getCreateTime, startTime)
                .lt(Notify::getCreateTime, endTime)
        );

        List<UnReadedDTO> rtn = (List<UnReadedDTO>) ArrayUtils.copyPropertiesList(list, UnReadedDTO.class);

        Set<Long> ids = new HashSet<>();
        list.forEach(notify -> ids.add(notify.getSenderId()));

        if (ids.size() > 0) {
            List<Employee> employeeList = employeeServiceImpl.list(
                Wrappers.<Employee>lambdaQuery()
                    .select(Employee::getEmployeeId, Employee::getName)
                    .in(Employee::getEmployeeId, ids)
            );

            Map<Long, String> idNameMap = new HashMap<>(16);
            employeeList.forEach(employee -> idNameMap.put(employee.getEmployeeId(), employee.getName()));
            rtn.forEach(unreadedDTO -> unreadedDTO.setName(idNameMap.get(unreadedDTO.getSenderId())));
        }

        return rtn;
    }

    @Autowired
    private IEmployeeService employeeServiceImpl;
}
