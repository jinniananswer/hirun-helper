package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.microtomato.hirun.modules.bss.customer.entity.po.Project;
import com.microtomato.hirun.modules.bss.customer.mapper.ProjectMapper;
import com.microtomato.hirun.modules.bss.customer.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-15
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

}
