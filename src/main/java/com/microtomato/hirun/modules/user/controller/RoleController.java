package com.microtomato.hirun.modules.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.user.entity.po.Role;
import com.microtomato.hirun.modules.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 角色表
 * (归属组织的角色，职级的角色等) 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("api/user/role")
public class RoleController {

    @Autowired
    private IRoleService roleServiceImpl;

    /**
     *
     * startEndDate 格式：2019-12-04 - 2020-01-02
     *
     * @param rolename
     * @param startEndDate
     * @return
     */
    @GetMapping("role-list")
    @RestResult
    public List<Role> roleList(String rolename, String startEndDate) {

        LocalDate startDate = null;
        LocalDate endDate = null;

        if (StringUtils.isNotBlank(startEndDate)) {
            String[] split = StringUtils.splitByWholeSeparator(startEndDate, " - ");
            startDate = LocalDate.parse(split[0], Constants.dateTimeFormatter);
            endDate = LocalDate.parse(split[1], Constants.dateTimeFormatter);
        }

        // 超级工号不展示
        List<Role> roleList = roleServiceImpl.list(
            Wrappers.<Role>lambdaQuery()
                .select(Role::getRoleId, Role::getRoleName, Role::getRoleType, Role::getStatus, Role::getCreateTime)
                .ne(Role::getRoleId, Constants.SUPER_ROLE_ID)
                .between((null != startDate && null != endDate), Role::getCreateTime, startDate, endDate)
                .like(StringUtils.isNotBlank(rolename), Role::getRoleName, rolename)

        );

        return roleList;

    }

}
