package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionRelaMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionRelaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (CollegeQuestionRela)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
@Service("collegeQuestionRelaService")
public class CollegeQuestionRelaServiceImpl extends ServiceImpl<CollegeQuestionRelaMapper, CollegeQuestionRela> implements ICollegeQuestionRelaService {

    @Autowired
    private CollegeQuestionRelaMapper collegeQuestionRelaMapper;

    @Override
    public List<CollegeQuestionRela> queryByEmployeeIdAndRelaType(Long employeeId, String relationType) {
        return this.list(new QueryWrapper<CollegeQuestionRela>().lambda()
                .eq(CollegeQuestionRela::getEmployeeId, employeeId)
                .eq(StringUtils.isNotEmpty(relationType), CollegeQuestionRela::getRelationType, relationType)
                .eq(CollegeQuestionRela::getStatus, KnowhowConsts.NORMAL_STATUS_VALID));
    }
}