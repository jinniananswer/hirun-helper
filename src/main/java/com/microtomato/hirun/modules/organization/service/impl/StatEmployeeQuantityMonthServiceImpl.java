package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SecurityUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeQuantityMonthMapper;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.entity.consts.FuncConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class StatEmployeeQuantityMonthServiceImpl extends ServiceImpl<StatEmployeeQuantityMonthMapper, StatEmployeeQuantityMonth> implements IStatEmployeeQuantityMonthService {
    @Autowired
    private StatEmployeeQuantityMonthMapper mapper;

    @Autowired
    private IOrgService orgService;

    /**
     * 查询部门在某年某月的在职人员数
     *
     * @param year
     * @param month
     * @param orgId
     * @return
     */
    @Override
    public StatEmployeeQuantityMonth queryCountRecord(String year, String month, Long orgId) {
        StatEmployeeQuantityMonth statEmployeeQuantityMonth = this.mapper.selectOne(new QueryWrapper<StatEmployeeQuantityMonth>().lambda()
                .eq(StatEmployeeQuantityMonth::getYear, year)
                .eq(StatEmployeeQuantityMonth::getOrgId, orgId).eq(StatEmployeeQuantityMonth::getMonth, month));
        return statEmployeeQuantityMonth;
    }

    /**
     * 查询部门在职人员数记录
     *
     * @param year
     * @param orgId
     * @return
     */
    @Override
    public List<EmployeeQuantityStatDTO> queryEmployeeQuantityStat(String year, Long orgId) {
        //如果前台选择的年份为空，则默认设置为当前年份
        if (StringUtils.isEmpty(year)) {
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
        }
        UserContext userContext = WebContextUtils.getUserContext();
        Long userOrgId = userContext.getOrgId();
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, userOrgId);


        if (orgId == null) {
            if (SecurityUtils.hasFuncId(FuncConst.ALL_ORG)) {
                orgId = 122L;
            } else if (SecurityUtils.hasFuncId(FuncConst.ALL_CITY)) {
                orgId = 7L;
            } else if (SecurityUtils.hasFuncId(FuncConst.ALL_WOODEN)) {
                orgId = 9L;
            } else if (SecurityUtils.hasFuncId(FuncConst.ALL_SHOP)) {
                Org parentOrg = orgDO.findParent("2", orgService.listAllOrgs(), userOrgId);
                orgId = parentOrg.getOrgId();
            } else {
                orgId = userOrgId;
            }
        }

        String orgLine = orgDO.getOrgLine(orgId);

        LambdaQueryWrapper queryWrapper = Wrappers.<StatEmployeeQuantityMonth>lambdaQuery()
                .eq(StatEmployeeQuantityMonth::getYear, year)
                .in(StatEmployeeQuantityMonth::getOrgId, Arrays.asList(orgLine.split(",")));

        List<StatEmployeeQuantityMonth> recordList = this.mapper.selectList(queryWrapper);

        if (ArrayUtils.isEmpty(recordList)) {
            return new ArrayList<>();
        }
        //将查询结果按部门归类
        Map<Long, List<StatEmployeeQuantityMonth>> classifyMap = new HashMap<>();
        for (int i = 0; i < recordList.size(); i++) {
            StatEmployeeQuantityMonth record = recordList.get(i);

            if (!classifyMap.containsKey(record.getOrgId())) {
                List<StatEmployeeQuantityMonth> classifyList = new ArrayList<>();
                classifyList.add(record);
                classifyMap.put(record.getOrgId(), classifyList);
            } else {
                List<StatEmployeeQuantityMonth> tempList = classifyMap.get(record.getOrgId());
                tempList.add(record);
            }
        }

        Set<Long> keys = classifyMap.keySet();
        List<Map<String, String>> resultList = new ArrayList<>();
        //取每个部门的集合，将竖表变成横表
        for (Long key : keys) {
            List<StatEmployeeQuantityMonth> statEmployeeQuantityMonths = classifyMap.get(key);

            Map<String, String> resultMap = new HashMap<>();
            OrgDO resultOrgDo = SpringContextUtils.getBean(OrgDO.class, key);
            resultMap.put("orgName", resultOrgDo.getCompanyLinePath());
            resultMap.put("orgId", key + "");
            resultMap.put("parentOrgId", resultOrgDo.getParentOrgId() + "");


            for (int i = 0; i < statEmployeeQuantityMonths.size(); i++) {
                StatEmployeeQuantityMonth statEmployeeQuantityMonth = statEmployeeQuantityMonths.get(i);
                resultMap.put("count_" + statEmployeeQuantityMonth.getMonth(), statEmployeeQuantityMonth.getEmployeeQuantity() + "");
            }
            resultList.add(resultMap);
        }
        List<EmployeeQuantityStatDTO> list = getTreeList(resultList);
        return list;
    }


    private List<EmployeeQuantityStatDTO> getTreeList(List<Map<String, String>> list) {
        if (ArrayUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<EmployeeQuantityStatDTO> nodes = new ArrayList<>();

        for (Map<String, String> employeeStat : list) {
            EmployeeQuantityStatDTO node = new EmployeeQuantityStatDTO();
            node = mapToDTO(employeeStat, node);

            if (node.getParentOrgId() == null) {
                node.setSpread(true);
            }
            node.setNode(node);
            nodes.add(node);
        }

        List<EmployeeQuantityStatDTO> tree = buildTree(nodes);

        return tree;
    }

    private void buildChildren(EmployeeQuantityStatDTO root, List<EmployeeQuantityStatDTO> nodes) {
        List<EmployeeQuantityStatDTO> children = new ArrayList<>();

        for (EmployeeQuantityStatDTO dto : nodes) {
            if (dto.getParentOrgId() == null) {
                continue;
            }
            if (dto.getParentOrgId().equals(root.getOrgId())) {
                children.add(dto);
                buildChildren(dto, nodes);
            }
        }

        if (ArrayUtils.isNotEmpty(children)) {
            root.setStatDTOS(children);
        }
    }

    private List<EmployeeQuantityStatDTO> buildTree(List<EmployeeQuantityStatDTO> list) {
        List<EmployeeQuantityStatDTO> roots = findRoot(list);
        if (ArrayUtils.isEmpty(roots)) {
            return new ArrayList<>();
        }
        for (EmployeeQuantityStatDTO rootDto : roots) {
            buildChildren(rootDto, list);
        }
        return roots;
    }

    private List<EmployeeQuantityStatDTO> findRoot(List<EmployeeQuantityStatDTO> list) {
        List<EmployeeQuantityStatDTO> roots = new ArrayList<>();
        for (EmployeeQuantityStatDTO dto : list) {
            if (dto.getParentOrgId() == null) {
                roots.add(dto);
            }
        }
        return roots;
    }

    private EmployeeQuantityStatDTO mapToDTO(Map<String, String> map, EmployeeQuantityStatDTO employeeQuantityStatDTO) {
        if (StringUtils.isEmpty(map.get("count_1"))) {
            employeeQuantityStatDTO.setJanurayCount(0);
        } else {
            employeeQuantityStatDTO.setJanurayCount(Integer.parseInt(map.get("count_1")));
        }
        if (StringUtils.isEmpty(map.get("count_2"))) {
            employeeQuantityStatDTO.setFebruaryCount(0);
        } else {
            employeeQuantityStatDTO.setFebruaryCount(Integer.parseInt(map.get("count_2")));
        }
        if (StringUtils.isEmpty(map.get("count_3"))) {
            employeeQuantityStatDTO.setMarchCount(0);
        } else {
            employeeQuantityStatDTO.setMarchCount(Integer.parseInt(map.get("count_3")));
        }
        if (StringUtils.isEmpty(map.get("count_4"))) {
            employeeQuantityStatDTO.setAprilCount(0);
        } else {
            employeeQuantityStatDTO.setAprilCount(Integer.parseInt(map.get("count_4")));
        }
        if (StringUtils.isEmpty(map.get("count_5"))) {
            employeeQuantityStatDTO.setMayCount(0);
        } else {
            employeeQuantityStatDTO.setMayCount(Integer.parseInt(map.get("count_5")));
        }
        if (StringUtils.isEmpty(map.get("count_6"))) {
            employeeQuantityStatDTO.setJuneCount(0);
        } else {
            employeeQuantityStatDTO.setJuneCount(Integer.parseInt(map.get("count_6")));
        }
        if (StringUtils.isEmpty(map.get("count_7"))) {
            employeeQuantityStatDTO.setJulyCount(0);
        } else {
            employeeQuantityStatDTO.setJulyCount(Integer.parseInt(map.get("count_7")));
        }
        if (StringUtils.isEmpty(map.get("count_8"))) {
            employeeQuantityStatDTO.setAugustCount(0);
        } else {
            employeeQuantityStatDTO.setAugustCount(Integer.parseInt(map.get("count_8")));
        }
        if (StringUtils.isEmpty(map.get("count_9"))) {
            employeeQuantityStatDTO.setSeptemberCount(0);
        } else {
            employeeQuantityStatDTO.setSeptemberCount(Integer.parseInt(map.get("count_9")));
        }
        if (StringUtils.isEmpty(map.get("count_10"))) {
            employeeQuantityStatDTO.setOctoberCount(0);
        } else {
            employeeQuantityStatDTO.setOctoberCount(Integer.parseInt(map.get("count_10")));
        }
        if (StringUtils.isEmpty(map.get("count_11"))) {
            employeeQuantityStatDTO.setNovemberCount(0);
        } else {
            employeeQuantityStatDTO.setNovemberCount(Integer.parseInt(map.get("count_11")));
        }
        if (StringUtils.isEmpty(map.get("count_12"))) {
            employeeQuantityStatDTO.setDecemberCount(0);
        } else {
            employeeQuantityStatDTO.setDecemberCount(Integer.parseInt(map.get("count_12")));
        }
        employeeQuantityStatDTO.setOrgId(Long.parseLong(map.get("orgId")));
        employeeQuantityStatDTO.setOrgName(map.get("orgName"));
        if (StringUtils.equals("null", map.get("parentOrgId"))) {
            employeeQuantityStatDTO.setParentOrgId(null);
        } else {
            employeeQuantityStatDTO.setParentOrgId(Long.parseLong(map.get("parentOrgId")));
        }
        return employeeQuantityStatDTO;
    }
}