package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.Action;
import com.microtomato.hirun.modules.bss.config.service.IActionService;
import com.microtomato.hirun.modules.bss.customer.entity.dto.*;
import com.microtomato.hirun.modules.bss.customer.entity.po.*;
import com.microtomato.hirun.modules.bss.customer.service.*;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Slf4j
@Service
public class CustomerDomainServiceImpl implements ICustomerDomainService {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IPartyService partyService;

    @Autowired
    private IActionService actionService;

    @Autowired
    private IBlueprintActionService blueprintActionService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private ICustOriginalActionService custOriginalActionService;

    @Autowired
    private IProjectOriginalActionService projectOriginalActionService;

    @Override
    public CustomerInfoDetailDTO queryCustomerInfoDetail(Long customerId, String openId, Long partyId) {

        Customer customer = customerService.queryCustomerInfo(customerId);
        CustomerInfoDetailDTO customerInfoDetailDTO = partyService.queryPartyInfoDetail(partyId);

        if (customer == null && customerInfoDetailDTO != null) {
            //家装顾问环节无信息，客户代表环节有信息
            this.transCustomerInfoDetail(customerInfoDetailDTO);
            return customerInfoDetailDTO;
        } else if (customer != null && customerInfoDetailDTO == null) {
            //家装顾问环节有信息，客户代表环节无信息
            CustomerInfoDetailDTO newDetailDTO = new CustomerInfoDetailDTO();
            BeanUtils.copyProperties(customer, newDetailDTO);
            newDetailDTO.setCustomerName(customer.getCustName());
            newDetailDTO.setHouseArea(customer.getHouseArea()+"");
            newDetailDTO.setHouseMode(staticDataService.getCodeName("HOUSE_MODE",customer.getHouseMode()));
            this.transCustomerInfoDetail(newDetailDTO);
            return newDetailDTO;
        } else if (customer != null && customerInfoDetailDTO != null) {
            //家装顾问环节有信息，客户代表环节有信息
            customerInfoDetailDTO.setHouseCounselorId(customer.getHouseCounselorId());
            customerInfoDetailDTO.setHouseId(customer.getHouseId());
            if (StringUtils.isEmpty(customerInfoDetailDTO.getCustomerName())) {
                customerInfoDetailDTO.setCustomerName(customer.getCustName());
            }
            if (StringUtils.isEmpty(customerInfoDetailDTO.getMobileNo())) {
                customerInfoDetailDTO.setMobileNo(customer.getMobileNo());
            }
            if (StringUtils.isEmpty(customerInfoDetailDTO.getHouseDetail())) {
                customerInfoDetailDTO.setHouseDetail(customer.getHouseDetail());
            }
            if (StringUtils.isEmpty(customerInfoDetailDTO.getHouseArea())) {
                customerInfoDetailDTO.setHouseArea(customer.getHouseArea() + "");
            }
            if (StringUtils.isEmpty(customerInfoDetailDTO.getHouseMode())) {
                customerInfoDetailDTO.setHouseMode(customer.getHouseMode());
            }
            if (StringUtils.isEmpty(customerInfoDetailDTO.getSex())) {
                customerInfoDetailDTO.setSex(customer.getSex() + "");
            }
            if (StringUtils.isEmpty(customerInfoDetailDTO.getHouseId() + "")) {
                customerInfoDetailDTO.setHouseId(customer.getHouseId());
            }
            this.transCustomerInfoDetail(customerInfoDetailDTO);
            return customerInfoDetailDTO;
        }

        return customerInfoDetailDTO;
    }

    /**
     * 转换静态数据
     *
     * @param customerInfoDetailDTO
     * @return
     */
    private CustomerInfoDetailDTO transCustomerInfoDetail(CustomerInfoDetailDTO customerInfoDetailDTO) {
        if (customerInfoDetailDTO == null) {
            return null;
        }
        if (StringUtils.isNotBlank(customerInfoDetailDTO.getHobby())) {
            String[] hobbys = customerInfoDetailDTO.getHobby().split(",");
            String hobbyName = "";
            for (int i = 0; i < hobbys.length; i++) {
                hobbyName += staticDataService.getCodeName("HOBBY", hobbys[i]) + "、";
            }
            customerInfoDetailDTO.setHobbyName(hobbyName.substring(0, hobbyName.length() - 1));
        }
        //转换信息来源
        if (StringUtils.isNotBlank(customerInfoDetailDTO.getInformationSource())) {
            String[] information = customerInfoDetailDTO.getInformationSource().split(",");
            String informationName = "";
            for (int i = 0; i < information.length; i++) {
                informationName += staticDataService.getCodeName("INFORMATIONSOURCE", information[i]) + "、";
            }
            customerInfoDetailDTO.setInformationSource(informationName.substring(0, informationName.length() - 1));
        }
        //翻译客户代表的名字
        if (customerInfoDetailDTO.getLinkEmployeeId() != null) {
            customerInfoDetailDTO.setCustomerServiceName(employeeService.getEmployeeNameEmployeeId(customerInfoDetailDTO.getLinkEmployeeId()));
        }
        //翻译家装顾问的名字
        if (customerInfoDetailDTO.getHouseCounselorId() != null) {
            customerInfoDetailDTO.setHouseCounselorName(employeeService.getEmployeeNameEmployeeId(customerInfoDetailDTO.getHouseCounselorId()));
        }
        //翻译楼盘名字
        if (customerInfoDetailDTO.getHouseId() != null) {
            customerInfoDetailDTO.setHouseName(housesService.queryHouseName(customerInfoDetailDTO.getHouseId()));
        }
        //翻译户型
        if (StringUtils.isNotBlank(customerInfoDetailDTO.getHouseMode())) {
            customerInfoDetailDTO.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE",customerInfoDetailDTO.getHouseMode()));
        }
        //翻译性别
        if (StringUtils.isNotBlank(customerInfoDetailDTO.getSex() + "")) {
            customerInfoDetailDTO.setSexName(staticDataService.getCodeName("SEX", customerInfoDetailDTO.getSex() + ""));
        }
        //模糊化电话号码和姓名
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        if (!employeeId.equals(customerInfoDetailDTO.getLinkEmployeeId()) && !employeeId.equals(customerInfoDetailDTO.getHouseCounselorId())) {
            customerInfoDetailDTO.setCustomerName(this.nameDesensitization(customerInfoDetailDTO.getCustomerName()));
            customerInfoDetailDTO.setMobileNo("***********");
        }
        return customerInfoDetailDTO;
    }

    /**
     * 动作完成信息查询
     *
     * @param customerId
     * @param openId
     * @param partyId
     * @return
     */
    @Override
    public List<CustomerActionInfoDTO> getActionInfo(Long customerId, String openId, Long partyId) {
        List<Action> actionList = actionService.queryActions("ALL");
        Customer customer=customerService.getById(customerId);
        Long houseCounselorId=0L;
        if(customer!=null){
            houseCounselorId=customer.getHouseCounselorId();
        }
        List<CustOriginalAction> custOriginalActions = custOriginalActionService.queryOriginalInfo(customerId, houseCounselorId);
        List<ProjectOriginalAction> projectOriginalActions = projectOriginalActionService.queryOriginalActionInfo(partyId);

        Map<String, CustomerActionInfoDTO> actionMap = new HashMap<String, CustomerActionInfoDTO>();
        //关注公众号信息处理
        if (StringUtils.isNotBlank(openId)) {
            actionMap.put("GZGZH_ALL", this.setValue("GZGZH_ALL", null));
        }
        //家装顾问动作完成映射
        if (custOriginalActions.size() > 0) {
            for (CustOriginalAction custOriginalAction : custOriginalActions) {
                if (StringUtils.equals(custOriginalAction.getActionCode(), "GZHGZ")) {
                    actionMap.put("GZGZH_ALL", this.setValue("GZGZH_ALL", custOriginalAction.getFinishTime()));
                    continue;
                } else if (StringUtils.equals(custOriginalAction.getActionCode(), "LTZDSTS")) {
                    actionMap.put("LTZDSTS_ALL", this.setValue("LTZDSTS_ALL", custOriginalAction.getFinishTime()));
                    continue;
                } else if (StringUtils.equals(custOriginalAction.getActionCode(), "SMJRQLC")) {
                    actionMap.put("SMJRQLC_ALL", this.setValue("SMJRQLC_ALL", custOriginalAction.getFinishTime()));
                    continue;
                } else if (StringUtils.equals(custOriginalAction.getActionCode(), "XQLTYTS")) {
                    actionMap.put("XQLTYTS_ALL", this.setValue("XQLTYTS_ALL", custOriginalAction.getFinishTime()));
                    continue;
                }
            }
        }
        //客户代表动作完成映射
        if (projectOriginalActions.size() > 0) {
            for (ProjectOriginalAction projectOriginalAction : projectOriginalActions) {
                if (StringUtils.equals(projectOriginalAction.getActionCode(), "SMJRLC")) {
                    actionMap.put("KHDBBD_ALL", this.setValue("KHDBBD_ALL", projectOriginalAction.getFinishTime()));
                    actionMap.put("ZX_ALL", this.setValue("ZX_ALL", projectOriginalAction.getFinishTime()));
                    continue;
                } else if (StringUtils.equals(projectOriginalAction.getActionCode(), "DKCSMW")) {
                    actionMap.put("TYCSMW_ALL", this.setValue("TYCSMW_ALL", projectOriginalAction.getFinishTime()));
                    continue;
                } else if (StringUtils.equals(projectOriginalAction.getActionCode(), "APSJS")) {
                    actionMap.put("APSJS_ALL", this.setValue("APSJS_ALL", projectOriginalAction.getFinishTime()));
                    continue;
                }
            }
        }
        //补充风格蓝图和功能蓝图的动作完成情况
        List<BlueprintAction> list = blueprintActionService.queryBluePrintInfo(openId, "XQLTE");
        if (list.size() > 0) {
            BlueprintAction blueprintAction = list.get(0);
            if (blueprintAction.getFuncprintCreateTime() != null) {
                actionMap.put("GNLTETS_ALL", this.setValue("GNLTETS_ALL", blueprintAction.getFuncprintCreateTime()));
            }
            if (blueprintAction.getStyleprintCreateTime() != null) {
                actionMap.put("FGLTETS_ALL", this.setValue("FGLTETS_ALL", blueprintAction.getStyleprintCreateTime()));
            }
        }
        //补充客户代表完成的需求蓝图一情况
        if(!actionMap.containsKey("XQLTYTS_ALL")){
            List<BlueprintAction> xqltyList=blueprintActionService.queryBluePrintInfo(openId,"XQLTY,XQLTY_A,XQLTY_B,XQLTY_C");
            if(xqltyList.size()>0){
                BlueprintAction xqltyBlue = xqltyList.get(0);
                actionMap.put("XQLTYTS_ALL", this.setValue("XQLTYTS_ALL", TimeUtils.stringToLocalDateTime(xqltyBlue.getModeTime(),"yyyy-MM-dd HH:mm:ss")));
            }
        }

        //将未完成的动作加入到流程信息
        List<CustomerActionInfoDTO> result = new ArrayList<>();
        for (Action action : actionList) {
            if (actionMap.containsKey(action.getActionCode())) {
                CustomerActionInfoDTO actionInfoDTO = actionMap.get(action.getActionCode());
                actionInfoDTO.setActionName(action.getActionName());
                result.add(actionInfoDTO);
            } else {
                CustomerActionInfoDTO unfinishAction = new CustomerActionInfoDTO();
                unfinishAction.setActionName(action.getActionName());
                unfinishAction.setStatus("0");
                result.add(unfinishAction);
            }
        }

        return result;
    }

    private CustomerActionInfoDTO setValue(String actionCode, LocalDateTime finishTime) {
        CustomerActionInfoDTO actionInfoDTO = new CustomerActionInfoDTO();
        actionInfoDTO.setActionCode(actionCode);
        actionInfoDTO.setFinishDate(finishTime);
        actionInfoDTO.setStatus("1");
        actionInfoDTO.setColor("#0bbd87");
        return actionInfoDTO;
    }

    /**
     * 需求蓝图一信息
     *
     * @param openId
     * @return
     */
    @Override
    public List<XQLTYInfoDTO> getXQLTYInfo(String openId) {
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        List<BlueprintAction> list = blueprintActionService.queryBluePrintInfo(openId, "XQLTY,XQLTY_A,XQLTY_B,XQLTY_C");
        List<XQLTYInfoDTO> dtoList = new ArrayList<>();

        if (list.size() <= 0) {
            return dtoList;
        }

        for (BlueprintAction blueprintAction : list) {
            XQLTYInfoDTO xqltyInfoDTO = new XQLTYInfoDTO();
            BeanUtils.copyProperties(blueprintAction, xqltyInfoDTO);
            xqltyInfoDTO.setPushEmployeeName(employeeService.getEmployeeNameEmployeeId(blueprintAction.getRelEmployeeId()));
            if (StringUtils.equals(blueprintAction.getActionCode(), "XQLTY_A")) {
                xqltyInfoDTO.setActionTypeName("蓝图一(A)");
            } else if (StringUtils.equals(blueprintAction.getActionCode(), "XQLTY_B")) {
                xqltyInfoDTO.setActionTypeName("蓝图一(B)");
            } else if (StringUtils.equals(blueprintAction.getActionCode(), "XQLTY_C")) {
                xqltyInfoDTO.setActionTypeName("蓝图一(C)");
            } else {
                xqltyInfoDTO.setActionTypeName("蓝图一(A)");
            }
            dtoList.add(xqltyInfoDTO);
        }
        return dtoList;
    }

    /**
     * 需求蓝图二信息加载
     *
     * @param openId
     * @return
     */
    @Override
    public XQLTEInfoDTO getXQLTEInfo(String openId) {
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        List<BlueprintAction> blueprintActionList = blueprintActionService.queryBluePrintInfo(openId, "XQLTE");
        if (blueprintActionList.size() <= 0) {
            return null;
        }
        BlueprintAction blueprintAction = blueprintActionList.get(0);
        String style = blueprintAction.getStyle();
        XQLTEInfoDTO xqlteInfoDTO = new XQLTEInfoDTO();
        //得到风格蓝图内容
        if (StringUtils.isBlank(style) || StringUtils.equals("false", style)) {
            xqlteInfoDTO.setStyleInfo(new ArrayList<StyleInfoDTO>());
        } else {
            List<StyleInfoDTO> styleList = this.transStyle(style);
            xqlteInfoDTO.setStyleInfo(styleList);

            //得到风格蓝图url
            List<String> urls = this.getStyleUrls(style);
            xqlteInfoDTO.setStyleUrl(urls);
        }

        //构造功能蓝图树结构
        Map<String,List<TreeNode>> funcMap=new HashMap<>();

        if(StringUtils.isNotBlank(blueprintAction.getFunc())&&!StringUtils.equals(blueprintAction.getFunc(),"false")){
            List<TreeNode> funcA=blueprintActionService.buildFuncTree(blueprintAction.getFunc(),"A");
            funcMap.put("FUNC_A",funcA);
        }

        if(StringUtils.isNotBlank(blueprintAction.getFuncB())&&!StringUtils.equals(blueprintAction.getFuncB(),"false")){
            List<TreeNode> funcB=blueprintActionService.buildFuncTree(blueprintAction.getFuncB(),"B");
            funcMap.put("FUNC_B",funcB);
        }

        if(StringUtils.isNotBlank(blueprintAction.getFuncC())&&!StringUtils.equals(blueprintAction.getFuncC(),"false")){
            List<TreeNode> funcC=blueprintActionService.buildFuncTree(blueprintAction.getFuncC(),"C");
            funcMap.put("FUNC_C",funcC);
        }

        xqlteInfoDTO.setFuncInfo(funcMap);
        return xqlteInfoDTO;
    }

    @Override
    public IPage<CustomerInfoDetailDTO> queryCustomerInfo(QueryCustCondDTO condDTO) {
        if(StringUtils.equals(condDTO.getBusiStatus(),"1")){
            return  partyService.queryCustomerInfo(condDTO);
        }else if(StringUtils.equals(condDTO.getBusiStatus(),"2")){
            return  customerService.queryCustomerInfoDetail(condDTO);
        }

        return null;
    }


    /**
     * 转换家网的风格蓝图数据
     *
     * @param style
     * @return
     */
    private List<StyleInfoDTO> transStyle(String style) {

        JSONArray styleArray = JSONArray.parseArray(style);
        int size = styleArray.size();

        List<StyleInfoDTO> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            JSONObject styleJsonObject = styleArray.getJSONObject(i);
            StyleInfoDTO styleInfoDTO = new StyleInfoDTO();
            styleInfoDTO.setName(styleJsonObject.getString("name"));
            styleInfoDTO.setContent(styleJsonObject.getString("content"));
            list.add(styleInfoDTO);
        }
        return list;
    }

    /**
     * 获取风格蓝图的图片
     *
     * @param style
     * @return
     */
    private List<String> getStyleUrls(String style) {
        JSONArray styleArray = JSONArray.parseArray(style);
        int size = styleArray.size();

        List<String> urls = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            JSONObject styleJsonObject = styleArray.getJSONObject(i);
            //获取每个风格蓝图的url
            JSONArray projects = styleJsonObject.getJSONArray("project");
            String url = "";

            if (StringUtils.equals(projects.toJSONString(), "false") || StringUtils.isBlank(projects.toJSONString())) {
                continue;
            } else {
                int projectSize = projects.size();
                for (int j = 0; j < projectSize; j++) {
                    JSONObject project = projects.getJSONObject(j);
                    url = project.getString("docs");
                    urls.add(url);
                }
            }
            urls.add(url);
        }
        return urls;
    }

    /**
     * 模糊化客户姓名
     *
     * @param name
     * @return
     */
    private String nameDesensitization(String name) {
        String newName = "";
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        name=name.replaceAll(regEx,"");
        if (StringUtils.isBlank(name)) {
            return "";
        }
        char[] chars = name.toCharArray();
        if (chars.length == 1) {
            newName = name;
        } else if (chars.length == 2) {
            newName = name.replaceFirst(name.substring(1), "*");
        } else {
            newName = name.replaceAll(name.substring(1, chars.length - 1), "*");
        }
        return newName;
    }
}
