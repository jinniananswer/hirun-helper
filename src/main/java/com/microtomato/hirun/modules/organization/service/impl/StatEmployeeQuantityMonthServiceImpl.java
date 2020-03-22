package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeHistoryDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeHolidayDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeQuantityMonthMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    private IStatEmployeeQuantityMonthService statEmployeeQuantityMonthService;


    /**
     * 查询部门在某年某月的在职人员数
     *
     * @param year
     * @param month
     * @param orgId
     * @return
     */
    @Override
    public StatEmployeeQuantityMonth queryCountRecord(String year, String month, Long orgId, String jobRole, String jobRoleNature, String orgNature, String jobGrade) {
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

        if (StringUtils.isEmpty(jobGrade)) {
            queryWrapper.apply("(job_grade is null or job_grade='')");
        } else {
            queryWrapper.eq(StatEmployeeQuantityMonth::getJobGrade, jobGrade);
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
    public List<EmployeeQuantityStatDTO> queryEmployeeQuantityStat(String year, String orgId) {
        //如果前台选择的年份为空，则默认设置为当前年份
        if (StringUtils.isEmpty(year)) {
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
        }

        //选择部门为空，则根据权限计算默认可查询的部门数据
        String orgLine = "";
        if (StringUtils.isEmpty(orgId)) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
            //OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = orgId;
        }

        List<EmployeeQuantityStatDTO> recordList = this.mapper.countByOrgId(year, orgLine);

        //2020/03/23新增.核减休假人数
        try {
            this.cutHolidayEmployee(recordList, year);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

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
                resultMap.put("count_" + statEmployeeQuantityMonth.getMonth(), statEmployeeQuantityMonth.getEmployeeNum() + "");
                resultMap.put("orgName", statEmployeeQuantityMonth.getOrgName());
                resultMap.put("orgId", key + "");
                resultMap.put("parentOrgId", statEmployeeQuantityMonth.getParentOrgId() + "");
            }
            resultList.add(resultMap);
        }
        List<EmployeeQuantityStatDTO> list = turnToEntityList(resultList);
        List<TreeNode> treeNodeList = this.getTreeList(list);
        List<EmployeeQuantityStatDTO> lastList = new ArrayList<>();
        last(treeNodeList, lastList);
        if (lastList.size() <= 0) {
            return null;
        }
        for (EmployeeQuantityStatDTO dto : lastList) {
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, dto.getOrgId());
            dto.setOrgName(orgDO.getCompanyLinePath());
        }
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

    /**
     * 计算每个节点的最终值
     *
     * @param nodeList
     * @param list
     */
    public void last(List<TreeNode> nodeList, List<EmployeeQuantityStatDTO> list) {
        if (ArrayUtils.isEmpty(nodeList)) {
            return;
        }
        for (TreeNode treeNode : nodeList) {
            EmployeeQuantityStatDTO dto = (EmployeeQuantityStatDTO) treeNode.getNode();

            if (ArrayUtils.isEmpty(treeNode.getChildren())) {
                list.add(dto);
            } else {
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
        parentDTO.setJanurayCount(parentDTO.getJanurayCount() + childDTO.getJanurayCount());
        parentDTO.setMarchCount(parentDTO.getMarchCount() + childDTO.getMarchCount());
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
            employeeQuantityStatDTO.setJanurayCount(0f);
        } else {
            employeeQuantityStatDTO.setJanurayCount(Float.parseFloat(map.get("count_1")));
        }
        if (StringUtils.isEmpty(map.get("count_2"))) {
            employeeQuantityStatDTO.setFebruaryCount(0f);
        } else {
            employeeQuantityStatDTO.setFebruaryCount(Float.parseFloat(map.get("count_2")));
        }
        if (StringUtils.isEmpty(map.get("count_3"))) {
            employeeQuantityStatDTO.setMarchCount(0f);
        } else {
            employeeQuantityStatDTO.setMarchCount(Float.parseFloat(map.get("count_3")));
        }
        if (StringUtils.isEmpty(map.get("count_4"))) {
            employeeQuantityStatDTO.setAprilCount(0f);
        } else {
            employeeQuantityStatDTO.setAprilCount(Float.parseFloat(map.get("count_4")));
        }
        if (StringUtils.isEmpty(map.get("count_5"))) {
            employeeQuantityStatDTO.setMayCount(0f);
        } else {
            employeeQuantityStatDTO.setMayCount(Float.parseFloat(map.get("count_5")));
        }
        if (StringUtils.isEmpty(map.get("count_6"))) {
            employeeQuantityStatDTO.setJuneCount(0f);
        } else {
            employeeQuantityStatDTO.setJuneCount(Float.parseFloat(map.get("count_6")));
        }
        if (StringUtils.isEmpty(map.get("count_7"))) {
            employeeQuantityStatDTO.setJulyCount(0f);
        } else {
            employeeQuantityStatDTO.setJulyCount(Float.parseFloat(map.get("count_7")));
        }
        if (StringUtils.isEmpty(map.get("count_8"))) {
            employeeQuantityStatDTO.setAugustCount(0f);
        } else {
            employeeQuantityStatDTO.setAugustCount(Float.parseFloat(map.get("count_8")));
        }
        if (StringUtils.isEmpty(map.get("count_9"))) {
            employeeQuantityStatDTO.setSeptemberCount(0f);
        } else {
            employeeQuantityStatDTO.setSeptemberCount(Float.parseFloat(map.get("count_9")));
        }
        if (StringUtils.isEmpty(map.get("count_10"))) {
            employeeQuantityStatDTO.setOctoberCount(0f);
        } else {
            employeeQuantityStatDTO.setOctoberCount(Float.parseFloat(map.get("count_10")));
        }
        if (StringUtils.isEmpty(map.get("count_11"))) {
            employeeQuantityStatDTO.setNovemberCount(0f);
        } else {
            employeeQuantityStatDTO.setNovemberCount(Float.parseFloat(map.get("count_11")));
        }
        if (StringUtils.isEmpty(map.get("count_12"))) {
            employeeQuantityStatDTO.setDecemberCount(0f);
        } else {
            employeeQuantityStatDTO.setDecemberCount(Float.parseFloat(map.get("count_12")));
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

    /**
     * 员工动态汇总统计
     *
     * @param time
     * @param orgId
     * @return
     */
    @Override
    public List<Map<String, String>> queryEmployeeTrendsStat(String time, String orgId, String orgNature) throws Exception {
        String year = "";
        String month = "";
        String orgLine = "";
        LocalDate endTime = null;
        Calendar nowDate = Calendar.getInstance();
        String nowYear = String.valueOf(nowDate.get(Calendar.YEAR));
        String nowMonth = String.valueOf(nowDate.get(Calendar.MONTH) + 1);

        if (StringUtils.isNotEmpty(time)) {
            year = time.split("-")[0];
            month = time.split("-")[1];
            if (month.contains("0")) {
                month = month.substring(1, 2);
            }
            //2020/03/20新增
            if (StringUtils.equals(year, nowYear) && StringUtils.equals(month, nowMonth)) {
                endTime = LocalDate.now();
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                Date date = simpleDateFormat.parse(time);
                endTime = TimeUtils.lastThisMonth(date).plusDays(1L);
            }

        } else {
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
            month = String.valueOf(date.get(Calendar.MONTH) + 1);
            endTime = LocalDate.now();
        }
        if (StringUtils.isEmpty(orgId)) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
/*            OrgDO orgDo = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = orgDo.getOrgLine(orgId);*/
            orgLine = orgId;
        }
        if (StringUtils.isEmpty(orgNature)) {
            orgNature = "1,2,3,4,5,6,7,8,9";
        }
        List<Map<String, String>> trndsList = this.mapper.countByOrgNatureAndJobRole(year, month, orgLine, orgNature);

        if (ArrayUtils.isEmpty(trndsList)) {
            return new ArrayList<>();
        }
        //将查询结果按照部门性质分类
        LinkedHashMap<String, List<Map<String, String>>> tempMap = new LinkedHashMap<>();
        for (Map<String, String> trndsMap : trndsList) {
            if (StringUtils.isEmpty(trndsMap.get("job_role")) || StringUtils.equals(trndsMap.get("job_role"), "9999")) {
                continue;
            }
            if (!tempMap.containsKey(trndsMap.get("org_nature"))) {
                List<Map<String, String>> tempList = new ArrayList<>();
                tempList.add(trndsMap);
                tempMap.put(trndsMap.get("org_nature"), tempList);
            } else {
                List<Map<String, String>> tempList1 = tempMap.get(trndsMap.get("org_nature"));
                tempList1.add(trndsMap);
            }
        }

        Set<String> keys = tempMap.keySet();
        List<Map<String, String>> resultList = new ArrayList<>();
        //取每个同质性部门的数据集合
        for (String key : keys) {
            List<Map<String, String>> resultMapList = tempMap.get(key);

            for (int i = 0; i < resultMapList.size(); i++) {
                Map<String, String> resultMap = resultMapList.get(i);
                resultMap.put("org_nature_name", this.staticDataService.getCodeName("ORG_NATURE", resultMap.get("org_nature")));
                resultMap.put("job_role_name", this.staticDataService.getCodeName("JOB_ROLE", resultMap.get("job_role")));
                resultMap.put("destroyEmployeeName", this.transEmployeeName(resultMap.get("destroy_employee_ids")));
                //resultMap.put("holidayEmployeeName", this.transEmployeeName(resultMap.get("holiday_employee_ids")));
                resultMap.put("entryEmployeeName", this.transEmployeeName(resultMap.get("entry_employee_ids")));
                resultMap.put("transInEmployeeName", this.transEmployeeName(resultMap.get("trans_in_employee_ids")));
                resultMap.put("transOutEmployeeName", this.transEmployeeName(resultMap.get("trans_out_employee_ids")));
                resultMap.put("borrowInEmployeeName", this.transEmployeeName(resultMap.get("borrow_in_employee_ids")));
                resultMap.put("borrowOutEmployeeName", this.transEmployeeName(resultMap.get("borrow_out_employee_ids")));
                resultList.add(resultMap);
            }
        }
        //2020/03/20新增
        List<EmployeeHolidayDTO> holidayList = this.baseMapper.countEmployeeHolidayInfo(endTime + "");
        if (holidayList.size() <= 0) {
            return resultList;
        }
        for (EmployeeHolidayDTO holidayDTO : holidayList) {
            String holidayJobRole = holidayDTO.getJobRole();
            String holidayJobGrade = holidayDTO.getJobGrade();
            String holidayJobRoleNature = holidayDTO.getJobRoleNature();
            String holidayOrgNature = holidayDTO.getOrgNature();
            String monthFlag = holidayDTO.getEmployeeInMonth();

            for (int i = 0; i < resultList.size(); i++) {
                String resultJobRole = resultList.get(i).get("job_role");
/*                String resultJobGrade=resultList.get(i).get("job_grade");
                String resultJobRoleNature=resultList.get(i).get("job_role_nature");*/
                String resultOrgNature = resultList.get(i).get("org_nature");
                if (StringUtils.equals(holidayJobRole, resultJobRole)
                        && StringUtils.equals(holidayOrgNature, resultOrgNature)) {
                    if (StringUtils.isBlank(resultList.get(i).get("employee_holiday_quantity_last"))) {
                        resultList.get(i).put("employee_holiday_quantity_last", "1");
                    } else {
                        resultList.get(i).put("employee_holiday_quantity_last", (Integer.parseInt(resultList.get(i).get("employee_holiday_quantity_last")) + 1) + "");
                    }
                    if (StringUtils.isBlank(resultList.get(i).get("holidayEmployeeName"))) {
                        resultList.get(i).put("holidayEmployeeName", employeeService.getEmployeeNameEmployeeId(holidayDTO.getEmployeeId()));
                    } else {
                        resultList.get(i).put("holidayEmployeeName", employeeService.getEmployeeNameEmployeeId(holidayDTO.getEmployeeId()) + "," + resultList.get(i).get("holidayEmployeeName"));
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 家装分公司统计四大业务线人员
     *
     * @param time
     * @return
     */
    @Override
    public List<Map<String, String>> queryEmployeeCompanyStat(String time, String orgNature, String jobRole) {
        String year = "";
        String month = "";
        String orgLine = "";

        if (StringUtils.isNotEmpty(time)) {
            year = time.split("-")[0];
            month = time.split("-")[1];
            if (month.contains("0")) {
                month = month.substring(1, 2);
            }
        } else {
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
            month = String.valueOf(date.get(Calendar.MONTH) + 1);
        }
        //只统计家装分公司，所以加上了事业部限制
        OrgDO orgDo = SpringContextUtils.getBean(OrgDO.class, 7L);
        orgLine = orgDo.getOrgLine(7L);
        //支持按照部门性质以及岗位进行统计
        if (StringUtils.isEmpty(orgNature)) {
            orgNature = "2,3,4,5,6,7";
        }

        if (StringUtils.isEmpty(jobRole)) {
            List<StaticData> dataList = staticDataService.getStaticDatas("JOB_ROLE");
            for (StaticData staticData : dataList) {
                jobRole += staticData.getCodeValue() + ",";
            }
            jobRole = jobRole.substring(0, jobRole.length() - 1);
        }

        List<Map<String, String>> companyCountList = this.mapper.companyCountByOrgNatureAndJobRoleAndCity(year, month, orgLine, orgNature, jobRole);
        if (ArrayUtils.isEmpty(companyCountList)) {
            return new ArrayList<>();
        }

        LinkedHashMap<String, List<Map<String, String>>> tempMap = new LinkedHashMap<>();
        for (Map<String, String> companyMap : companyCountList) {

            if (StringUtils.isEmpty(companyMap.get("job_role")) || StringUtils.equals(companyMap.get("job_role"), "9999")) {
                continue;
            }

            if (!tempMap.containsKey(companyMap.get("org_nature") + companyMap.get("job_role"))) {
                List<Map<String, String>> mapList = new ArrayList<>();
                mapList.add(companyMap);
                tempMap.put(companyMap.get("org_nature") + companyMap.get("job_role"), mapList);
            } else {
                List<Map<String, String>> tempList = tempMap.get(companyMap.get("org_nature") + companyMap.get("job_role"));
                tempList.add(companyMap);
            }
        }

        Set<String> keys = tempMap.keySet();
        List<Map<String, String>> resultList = new ArrayList<>();
        //取每个同质性部门的数据集合
        for (String key : keys) {
            List<Map<String, String>> resultMapList = tempMap.get(key);

            Map<String, String> resultMap = new HashMap<>();
            float employeeSum = 0f;
            for (int i = 0; i < resultMapList.size(); i++) {
                employeeSum += Float.parseFloat(resultMapList.get(i).get("employee_num"));
                resultMap.put("city_count_" + resultMapList.get(i).get("enterprise_id"), resultMapList.get(i).get("employee_num"));
                resultMap.put("org_nature_name", this.staticDataService.getCodeName("ORG_NATURE", resultMapList.get(i).get("org_nature")));
                resultMap.put("job_role_name", this.staticDataService.getCodeName("JOB_ROLE", resultMapList.get(i).get("job_role")));
            }
            resultMap.put("employee_sum", employeeSum + "");
            resultList.add(resultMap);
        }
        //将数据为空的全部改成0
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, String> lastMap = resultList.get(i);
            if (lastMap.get("city_count_1") == null) {
                lastMap.put("city_count_1", "0");
            }
            if (lastMap.get("city_count_2") == null) {
                lastMap.put("city_count_2", "0");
            }
            if (lastMap.get("city_count_3") == null) {
                lastMap.put("city_count_3", "0");
            }
            if (lastMap.get("city_count_16") == null) {
                lastMap.put("city_count_16", "0");
            }
            if (lastMap.get("city_count_4") == null) {
                lastMap.put("city_count_4", "0");
            }
            if (lastMap.get("city_count_5") == null) {
                lastMap.put("city_count_5", "0");
            }
            if (lastMap.get("city_count_6") == null) {
                lastMap.put("city_count_6", "0");
            }
            if (lastMap.get("city_count_7") == null) {
                lastMap.put("city_count_7", "0");
            }
            if (lastMap.get("city_count_8") == null) {
                lastMap.put("city_count_8", "0");
            }
            if (lastMap.get("city_count_9") == null) {
                lastMap.put("city_count_9", "0");
            }
            if (lastMap.get("city_count_10") == null) {
                lastMap.put("city_count_10", "0");
            }
            if (lastMap.get("city_count_11") == null) {
                lastMap.put("city_count_11", "0");
            }
            if (lastMap.get("city_count_12") == null) {
                lastMap.put("city_count_12", "0");
            }
            if (lastMap.get("city_count_13") == null) {
                lastMap.put("city_count_13", "0");
            }
            if (lastMap.get("city_count_15") == null) {
                lastMap.put("city_count_15", "0");
            }

        }
        return resultList;
    }

    /**
     * 四大业务类人员人数趋势
     *
     * @param time
     * @param orgId
     * @return
     */
    @Override
    public List<Map<String, String>> busiCountByOrgNatureAndJobRole(String time, String orgId, String orgNature) {
        if (StringUtils.isEmpty(time)) {
            Calendar date = Calendar.getInstance();
            time = String.valueOf(date.get(Calendar.YEAR));
        }

        //选择部门为空，则根据权限计算默认可查询的部门数据
        String orgLine = "";
        if (StringUtils.isEmpty(orgId)) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
/*            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = orgDO.getOrgLine(orgId);*/
            orgLine = orgId;
        }

        if (StringUtils.isEmpty(orgNature)) {
            orgNature = "2,3,4,5,6,7";
        }

        List<Map<String, String>> busiCountList = this.mapper.busiCountByOrgNatureAndJobRole(time, orgNature, orgLine);

        if (ArrayUtils.isEmpty(busiCountList)) {
            return new ArrayList<>();
        }

        LinkedHashMap<String, List<Map<String, String>>> tempMap = new LinkedHashMap<>();
        for (Map<String, String> companyMap : busiCountList) {
            if (StringUtils.isEmpty(companyMap.get("job_role")) || StringUtils.equals(companyMap.get("job_role"), "9999")) {
                continue;
            }
            if (!tempMap.containsKey(companyMap.get("org_nature") + companyMap.get("job_role"))) {
                List<Map<String, String>> mapList = new ArrayList<>();
                mapList.add(companyMap);
                tempMap.put(companyMap.get("org_nature") + companyMap.get("job_role"), mapList);
            } else {
                List<Map<String, String>> tempList = tempMap.get(companyMap.get("org_nature") + companyMap.get("job_role"));
                tempList.add(companyMap);
            }
        }

        Set<String> keys = tempMap.keySet();
        List<Map<String, String>> resultList = new ArrayList<>();
        //取每个同质性部门的数据集合
        for (String key : keys) {
            List<Map<String, String>> resultMapList = tempMap.get(key);
            Map<String, String> resultMap = new HashMap<>();

            for (int i = 0; i < resultMapList.size(); i++) {
                resultMap.put("org_nature_name", this.staticDataService.getCodeName("ORG_NATURE", resultMapList.get(i).get("org_nature")));
                resultMap.put("job_role_name", this.staticDataService.getCodeName("JOB_ROLE", resultMapList.get(i).get("job_role")));
                if (StringUtils.isEmpty(resultMapList.get(i).get("employee_num") + "")) {
                    resultMap.put("employee_num_" + resultMapList.get(i).get("month"), "0");
                } else {
                    resultMap.put("employee_num_" + resultMapList.get(i).get("month"), resultMapList.get(i).get("employee_num"));
                }
                if (StringUtils.isEmpty(resultMapList.get(i).get("employee_entry_quantity") + "")) {
                    resultMap.put("employee_entry_num_" + resultMapList.get(i).get("month"), "0");
                } else {
                    resultMap.put("employee_entry_num_" + resultMapList.get(i).get("month"), resultMapList.get(i).get("employee_entry_quantity"));
                }
                if (StringUtils.isEmpty(resultMapList.get(i).get("employee_destroy_quantity") + "")) {
                    resultMap.put("employee_destroy_num_" + resultMapList.get(i).get("month"), "0");
                } else {
                    resultMap.put("employee_destroy_num_" + resultMapList.get(i).get("month"), resultMapList.get(i).get("employee_destroy_quantity"));
                }
            }
            resultList.add(resultMap);
        }

        return resultList;
    }

    /**
     * 四大业务类人员与部门所有员工异动人数趋势
     *
     * @param time
     * @return
     */
    @Override
    public List<Map<String, String>> busiAndAllCountTrend(String time, String orgId) {
        String year = "";
        if (StringUtils.isEmpty(time)) {
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
        } else {
            year = time;
        }

        //选择部门为空，则根据权限计算默认可查询的部门数据
        String orgLine = "";
        if (StringUtils.isEmpty(orgId)) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
/*            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = orgDO.getOrgLine(orgId);*/
            orgLine = orgId;
        }

        List<Map<String, String>> busiAndAllCountTrendList = this.mapper.busiAndAllCountTrend(year, orgLine);
        if (ArrayUtils.isEmpty(busiAndAllCountTrendList)) {
            return new ArrayList<>();
        }

/*        //将查询结果按部门归类
        LinkedHashMap<String, List<Map<String, String>>> classifyMap = new LinkedHashMap<>();
        for (int i = 0; i < busiAndAllCountTrendList.size(); i++) {
            Map<String, String> record = busiAndAllCountTrendList.get(i);
            if (!classifyMap.containsKey(String.valueOf(record.get("org_id")))) {
                List<Map<String, String>> classifyList = new ArrayList<>();
                classifyList.add(record);
                classifyMap.put(String.valueOf(record.get("org_id")), classifyList);
            } else {
                List<Map<String, String>> tempList = classifyMap.get(record.get("org_id"));
                tempList.add(record);
            }
        }

        Set<String> keys = classifyMap.keySet();
        List<Map<String, String>> resultList = new ArrayList<>();
        //取每个部门的集合，将竖表变成横表
        for (String key : keys) {
            List<Map<String, String>> mapList = classifyMap.get(key);

            Map<String, String> resultMap = new HashMap<>();
            for (int i = 0; i < mapList.size(); i++) {

                if (mapList.get(i).get("month") == null) {
                    continue;
                }
                Map<String, String> busiAndAllCountTrend = mapList.get(i);
                resultMap.put("org_id", busiAndAllCountTrend.get("org_id"));
                resultMap.put("parent_org_id", busiAndAllCountTrend.get("parent_org_id"));
                resultMap.put("all_employee_entry_" + busiAndAllCountTrend.get("month"), busiAndAllCountTrend.get("all_employee_entry_quantity"));
                resultMap.put("all_employee_destroy_" + busiAndAllCountTrend.get("month"), busiAndAllCountTrend.get("all_employee_destroy_quantity"));
                resultMap.put("busi_employee_destroy_" + busiAndAllCountTrend.get("month"), busiAndAllCountTrend.get("busi_employee_destroy_quantity"));
                resultMap.put("busi_employee_entry_" + busiAndAllCountTrend.get("month"), busiAndAllCountTrend.get("busi_employee_entry_quantity"));
            }
            resultList.add(resultMap);
        }*/

        List<Org> shopOrgList = orgService.list(new QueryWrapper<Org>().lambda().eq(Org::getType, "4").eq(Org::getStatus, "0"));

        List<Map<String, String>> shopList = new ArrayList<>();
        for (Org org : shopOrgList) {

            LinkedHashMap<String, String> shopMap = new LinkedHashMap<>();
            shopMap.put("org_name", org.getName());

            for (int i = 0; i < busiAndAllCountTrendList.size(); i++) {
                Map<String, String> resultRecord = busiAndAllCountTrendList.get(i);
                if (StringUtils.equals(org.getOrgId() + "", resultRecord.get("parent_org_id"))) {
                    if (shopMap.get("all_employee_entry_" + resultRecord.get("month")) == null) {
                        shopMap.put("all_employee_entry_" + resultRecord.get("month"), resultRecord.get("all_employee_entry_quantity"));
                    } else {
                        float shopCount = Float.parseFloat(shopMap.get("all_employee_entry_" + resultRecord.get("month")));
                        float recordCount = Float.parseFloat(resultRecord.get("all_employee_entry_quantity"));
                        shopMap.put("all_employee_entry_" + resultRecord.get("month"), shopCount + recordCount + "");
                    }
                    if (shopMap.get("all_employee_destroy_" + resultRecord.get("month")) == null) {
                        shopMap.put("all_employee_destroy_" + resultRecord.get("month"), resultRecord.get("all_employee_destroy_quantity"));
                    } else {
                        float shopCount = Float.parseFloat(shopMap.get("all_employee_destroy_" + resultRecord.get("month")));
                        float recordCount = Float.parseFloat(resultRecord.get("all_employee_destroy_quantity"));
                        shopMap.put("all_employee_destroy_" + resultRecord.get("month"), shopCount + recordCount + "");
                    }

                    if (shopMap.get("busi_employee_entry_" + resultRecord.get("month")) == null) {
                        shopMap.put("busi_employee_entry_" + resultRecord.get("month"), resultRecord.get("busi_employee_entry_quantity"));
                    } else {
                        float shopCount = Float.parseFloat(shopMap.get("busi_employee_entry_" + resultRecord.get("month")));
                        if (resultRecord.get("busi_employee_entry_quantity") == null) {
                            resultRecord.put("busi_employee_entry_quantity", "0.0");
                        }
                        float recordCount = Float.parseFloat(resultRecord.get("busi_employee_entry_quantity"));
                        shopMap.put("busi_employee_entry_" + resultRecord.get("month"), shopCount + recordCount + "");
                    }

                    if (shopMap.get("busi_employee_destroy_" + resultRecord.get("month")) == null) {
                        shopMap.put("busi_employee_destroy_" + resultRecord.get("month"), resultRecord.get("busi_employee_destroy_quantity"));
                    } else {
                        float shopCount = Float.parseFloat(shopMap.get("busi_employee_destroy_" + resultRecord.get("month")));
                        if (resultRecord.get("busi_employee_destroy_quantity") == null) {
                            resultRecord.put("busi_employee_destroy_quantity", "0.0");
                        }
                        float recordCount = Float.parseFloat(resultRecord.get("busi_employee_destroy_quantity"));
                        shopMap.put("busi_employee_destroy_" + resultRecord.get("month"), shopCount + recordCount + "");
                    }
                } else {
                    continue;
                }
            }
            shopList.add(shopMap);

        }


        return shopList;
    }


    /**
     * 翻译异动员工姓名
     *
     * @param employeeId
     * @return
     */
    private String transEmployeeName(String employeeId) {
        if (StringUtils.isEmpty(employeeId)) {
            return "";
        }
        String[] employeeIds = employeeId.split(",");
        String employeeNames = "";
        for (int i = 0; i < employeeIds.length; i++) {
            employeeNames += employeeService.getEmployeeNameEmployeeId(Long.parseLong(employeeIds[i])) + ",";
        }
        return employeeNames.substring(0, employeeNames.length() - 1);
    }

    /**
     * 重新加载统计数据
     */
    @Override
    public void reloadCount() {
        List<EmployeeQuantityStatDTO> dtoList = employeeService.countEmployeeQuantityByOrgId();
        if (ArrayUtils.isEmpty(dtoList)) {
            return;
        }

        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String month = String.valueOf(date.get(Calendar.MONTH) + 1);

        List<StatEmployeeQuantityMonth> addList = new ArrayList<>();
        //删除原有记录
        this.mapper.delete(Wrappers.<StatEmployeeQuantityMonth>lambdaQuery().eq(StatEmployeeQuantityMonth::getMonth, month)
                .eq(StatEmployeeQuantityMonth::getYear, year));
        //重新新增
        for (EmployeeQuantityStatDTO dto : dtoList) {

            StatEmployeeQuantityMonth statEmployeeQuantityMonth = new StatEmployeeQuantityMonth();
            BeanUtils.copyProperties(dto, statEmployeeQuantityMonth);
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(dto.getJobGrade())) {
                statEmployeeQuantityMonth.setJobGrade("0");
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(dto.getJobRole())) {
                statEmployeeQuantityMonth.setJobRole("9999");
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(dto.getJobRoleNature())) {
                statEmployeeQuantityMonth.setJobRoleNature("0");
            }
            statEmployeeQuantityMonth.setYear(year);
            statEmployeeQuantityMonth.setMonth(month);
            addList.add(statEmployeeQuantityMonth);

        }
        if (ArrayUtils.isNotEmpty(addList)) {
            statEmployeeQuantityMonthService.saveBatch(addList);
        }
    }

    /**
     * 在岗人数=在编人数-休假人数
     *
     * @param dtos
     * @return
     */
    public List<EmployeeQuantityStatDTO> cutHolidayEmployee(List<EmployeeQuantityStatDTO> dtos, String year) throws Exception {
        if (ArrayUtils.isEmpty(dtos)) {
            return dtos;
        }
        Calendar date = Calendar.getInstance();
        int month = date.get(Calendar.MONTH) + 1;
        for (int i = 1; i <= month; i++) {
            LocalDate endTime = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            Date transDate = null;
            if (i < 10) {
                transDate = simpleDateFormat.parse(year + "-0" + i);
            }
            if (i >= 10) {
                transDate = simpleDateFormat.parse(year + "-" + i);
            }
            endTime = TimeUtils.lastThisMonth(transDate);

            List<EmployeeHolidayDTO> holidayList = this.baseMapper.countEmployeeHolidayInfo(endTime + "");
            if (ArrayUtils.isEmpty(holidayList)) {
                continue;
            }
            for (EmployeeHolidayDTO employeeHolidayDTO : holidayList) {
                Long orgId = employeeHolidayDTO.getOrgId();
                for (EmployeeQuantityStatDTO employeeQuantityStatDTO : dtos) {
                    Long statOrgId = employeeQuantityStatDTO.getOrgId();
                    String statMonth = employeeQuantityStatDTO.getMonth();
                    Float statEmployeeNum = employeeQuantityStatDTO.getEmployeeNum();
                    if (orgId.equals(statOrgId) && StringUtils.equals(statMonth, i + "")) {
                        employeeQuantityStatDTO.setEmployeeNum(statEmployeeNum - 1);
                    }
                }
            }

        }

        return dtos;
    }
}