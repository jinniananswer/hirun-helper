package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeQuantityMonthMapper;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public StatEmployeeQuantityMonth queryCountRecord(String year, String month, Long orgId, String jobRole, String jobRoleNature, String orgNature) {
        LambdaQueryWrapper<StatEmployeeQuantityMonth> queryWrapper = new QueryWrapper<StatEmployeeQuantityMonth>().lambda();
        if (StringUtils.isEmpty(jobRole)) {
            queryWrapper.isNull(StatEmployeeQuantityMonth::getJobRole);
        } else {
            queryWrapper.eq(StatEmployeeQuantityMonth::getJobRole, jobRole);
        }

        if (StringUtils.isEmpty(jobRoleNature)) {
            queryWrapper.apply("(job_role_nature is null or job_role_nature='')");
        } else {
            queryWrapper.eq(StatEmployeeQuantityMonth::getJobRoleNature, jobRoleNature);
        }

        if (StringUtils.isEmpty(orgNature)) {
            queryWrapper.apply("(org_nature is null or org_nature='')");
        } else {
            queryWrapper.eq(StatEmployeeQuantityMonth::getOrgNature, orgNature);
        }

        queryWrapper.eq(StatEmployeeQuantityMonth::getYear, year);
        queryWrapper.eq(StatEmployeeQuantityMonth::getOrgId, orgId);
        queryWrapper.eq(StatEmployeeQuantityMonth::getMonth, month);
        StatEmployeeQuantityMonth statEmployeeQuantityMonth = this.mapper.selectOne(queryWrapper);
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

        //选择部门为空，则根据权限计算默认可查询的部门数据
        String orgLine = "";
        if (orgId == null) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = orgDO.getOrgLine(orgId);
        }

        List<EmployeeQuantityStatDTO> recordList = this.mapper.countByOrgId(year, orgLine);

        if (ArrayUtils.isEmpty(recordList)) {
            return new ArrayList<>();
        }
        //将查询结果按部门归类
        Map<Long, List<EmployeeQuantityStatDTO>> classifyMap = new HashMap<>();
        for (int i = 0; i < recordList.size(); i++) {
            EmployeeQuantityStatDTO record = recordList.get(i);

            if (!classifyMap.containsKey(record.getOrgId())) {
                List<EmployeeQuantityStatDTO> classifyList = new ArrayList<>();
                classifyList.add(record);
                classifyMap.put(record.getOrgId(), classifyList);
            } else {
                List<EmployeeQuantityStatDTO> tempList = classifyMap.get(record.getOrgId());
                tempList.add(record);
            }
        }

        Set<Long> keys = classifyMap.keySet();
        List<Map<String, String>> resultList = new ArrayList<>();
        //取每个部门的集合，将竖表变成横表
        for (Long key : keys) {
            List<EmployeeQuantityStatDTO> statEmployeeQuantityMonths = classifyMap.get(key);

            Map<String, String> resultMap = new HashMap<>();
            for (int i = 0; i < statEmployeeQuantityMonths.size(); i++) {
                EmployeeQuantityStatDTO statEmployeeQuantityMonth = statEmployeeQuantityMonths.get(i);
                resultMap.put("count_" + statEmployeeQuantityMonth.getMonth(), statEmployeeQuantityMonth.getEmployeeSum() + "");
                resultMap.put("orgName", statEmployeeQuantityMonth.getOrgName());
                resultMap.put("orgId", key + "");
                resultMap.put("parentOrgId", statEmployeeQuantityMonth.getParentOrgId() + "");
            }
            resultList.add(resultMap);
        }
        List<EmployeeQuantityStatDTO> list = turnToEntityList(resultList);
        List<TreeNode> treeNodeList = this.getTreeList(list);
        List<EmployeeQuantityStatDTO> lastList=new ArrayList<>();
        last(treeNodeList,lastList);
        Collections.reverse(lastList);
        return lastList;
    }

    /**
     * 将Map结构转换成dto
     *
     * @param list
     * @return
     */
    private List<EmployeeQuantityStatDTO> turnToEntityList(List<Map<String, String>> list) {
        if (ArrayUtils.isEmpty(list)) {
            return null;
        }
        List<EmployeeQuantityStatDTO> employeeQuantityStatDTOList = new ArrayList<>();
        for (Map<String, String> employeeStat : list) {
            EmployeeQuantityStatDTO employeeQuantityStatDTO = new EmployeeQuantityStatDTO();
            employeeQuantityStatDTO = mapToDTO(employeeStat, employeeQuantityStatDTO);
            employeeQuantityStatDTOList.add(employeeQuantityStatDTO);
        }
        return employeeQuantityStatDTOList;
    }

    /**
     * 构造一颗树
     *
     * @param list
     * @return
     */
    private List<TreeNode> getTreeList(List<EmployeeQuantityStatDTO> list) {
        if (ArrayUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<TreeNode> nodes = new ArrayList<>();

        for (EmployeeQuantityStatDTO dto : list) {
            TreeNode node = new TreeNode();
            node.setId(dto.getOrgId() + "");

            if (dto.getParentOrgId() != null) {
                node.setParentId(dto.getParentOrgId() + "");
            } else {
                node.setSpread(true);
            }
            node.setNode(dto);
            nodes.add(node);
        }

        List<TreeNode> tree = TreeUtils.build(nodes);
        return tree;
    }

    public void last(List<TreeNode> nodeList, List<EmployeeQuantityStatDTO> list) {
        if (ArrayUtils.isEmpty(nodeList)) {
            return;
        }
        for (TreeNode treeNode : nodeList) {
            EmployeeQuantityStatDTO dto = (EmployeeQuantityStatDTO) treeNode.getNode();

            if(ArrayUtils.isEmpty(treeNode.getChildren()) ){
                list.add(dto);
            }else{
                sumCount(treeNode, dto);
                list.add(dto);
                List<TreeNode> childNodeList = treeNode.getChildren();
                last(childNodeList, list);
            }
        }
    }

    private void sumCount(TreeNode treeNode, EmployeeQuantityStatDTO employeeQuantityStatDTO) {
        if (treeNode == null) {
            return;
        }
        EmployeeQuantityStatDTO dto = (EmployeeQuantityStatDTO) treeNode.getNode();

        employeeQuantityStatDTO = getCount(employeeQuantityStatDTO, dto);

        if (null != treeNode.getChildren() && treeNode.getChildren().size() > 0) {
            List<TreeNode> childNodeList = treeNode.getChildren();
            for (TreeNode childNode : childNodeList) {
                sumCount(childNode, employeeQuantityStatDTO);
            }
        }
    }

    private EmployeeQuantityStatDTO getCount(EmployeeQuantityStatDTO parentDTO, EmployeeQuantityStatDTO childDTO) {
        parentDTO.setFebruaryCount(parentDTO.getFebruaryCount() + childDTO.getFebruaryCount());
        parentDTO.setJanurayCount(parentDTO.getJanurayCount() + childDTO.getFebruaryCount());
        parentDTO.setMarchCount(parentDTO.getMayCount() + childDTO.getMarchCount());
        parentDTO.setAprilCount(parentDTO.getAprilCount() + childDTO.getAprilCount());
        parentDTO.setMayCount(parentDTO.getMayCount() + childDTO.getMayCount());
        parentDTO.setJuneCount(parentDTO.getJuneCount() + childDTO.getJuneCount());
        parentDTO.setJulyCount(parentDTO.getJulyCount() + childDTO.getJulyCount());
        parentDTO.setAugustCount(parentDTO.getAugustCount() + childDTO.getAugustCount());
        parentDTO.setSeptemberCount(parentDTO.getSeptemberCount() + childDTO.getSeptemberCount());
        parentDTO.setOctoberCount(parentDTO.getOctoberCount() + childDTO.getOctoberCount());
        parentDTO.setDecemberCount(parentDTO.getDecemberCount() + childDTO.getDecemberCount());
        parentDTO.setNovemberCount(parentDTO.getNovemberCount() + childDTO.getNovemberCount());
        return parentDTO;
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