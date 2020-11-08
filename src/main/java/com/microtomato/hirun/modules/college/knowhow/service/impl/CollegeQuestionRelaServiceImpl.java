package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionRelaMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionRelaService;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public List<CollegeQuestionRela> queryByEmployeeIdAndRelaType(Long employeeId, String relationType) {
        Boolean flag = true;
        if (StringUtils.equals("2", relationType)) {
            List<StaticData> approveManagers = staticDataService.getStaticDatas("APPROVE_MANAGER");
            flag = false;
            if (ArrayUtils.isNotEmpty(approveManagers)) {
                flag = true;
                List<StaticData> collect = approveManagers.stream().filter(x ->
                        StringUtils.equals(x.getCodeValue(), String.valueOf(employeeId)))
                        .collect(Collectors.toList());
                if (ArrayUtils.isNotEmpty(collect)) {
                    flag = false;
                } else {
                    return new ArrayList<>();
                }
            }
        }

        return this.list(new QueryWrapper<CollegeQuestionRela>().lambda()
                .eq(flag, CollegeQuestionRela::getEmployeeId, employeeId)
                .eq(StringUtils.isNotEmpty(relationType), CollegeQuestionRela::getRelationType, relationType)
                .eq(CollegeQuestionRela::getStatus, KnowhowConsts.NORMAL_STATUS_VALID));
    }

    @Override
    public Long getEmployeeByQuestionId(Long questionId) {
        CollegeQuestionRela rela = this.getOne(new QueryWrapper<CollegeQuestionRela>().lambda()
                .eq(CollegeQuestionRela::getQuestionId, questionId)
                .eq(CollegeQuestionRela::getRelationType, "0")
                .eq(CollegeQuestionRela::getStatus, "1"));
        if (null == rela) {
            return null;
        }
        return rela.getEmployeeId();
    }
}