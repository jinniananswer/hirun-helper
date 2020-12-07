package com.microtomato.hirun.modules.bi.middleproduct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.PushDataStatisticDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.QueryPushDataDetailDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.QueryPushDataStatisticDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodSend;
import com.microtomato.hirun.modules.bi.middleproduct.mapper.MidprodSendMapper;
import com.microtomato.hirun.modules.bi.middleproduct.service.IMidprodSendService;
import com.microtomato.hirun.modules.organization.service.impl.OrgServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (MidprodSend)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:47
 */
@Service("midprodSendService")
public class MidprodSendServiceImpl extends ServiceImpl<MidprodSendMapper, MidprodSend> implements IMidprodSendService {

    @Autowired
    private MidprodSendMapper midprodSendMapper;

    @Autowired
    private OrgServiceImpl orgService;

    @Override
    public List<PushDataStatisticDTO> querySendCountData(QueryPushDataStatisticDTO dto) {
        QueryWrapper<QueryPushDataStatisticDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dto.getProductType()), "b.mode_id", dto.getProductType());
        queryWrapper.like(StringUtils.isNotBlank(dto.getEmployeeName()), "b.name", dto.getEmployeeName());
        queryWrapper.ge(dto.getStartTime() != null, "a.send_time", dto.getStartTime());
        queryWrapper.le(dto.getEndTime() != null, "a.send_time", dto.getEndTime());
        queryWrapper.apply(" a.employee_id=b.employee_id");
        queryWrapper.groupBy(" a.employee_id,b.name");
        queryWrapper.orderByDesc("a.shop_id");

        if (StringUtils.equals(dto.getQueryType(), "1")) {
            List<PushDataStatisticDTO> sendList = this.midprodSendMapper.querySendByEmployee(queryWrapper);
            if (sendList.size() > 0) {
                queryWrapper.apply("a.send_id=c.send_id");
                List<PushDataStatisticDTO> openList = this.midprodSendMapper.queryOpenByEmployee(queryWrapper);
                Map<Long, PushDataStatisticDTO> temp = new HashMap<>();

                if (ArrayUtils.isNotEmpty(openList)) {
                    for (PushDataStatisticDTO statisticDTO : openList) {
                        temp.put(statisticDTO.getEmployeeId(), statisticDTO);
                    }
                }

                for (PushDataStatisticDTO sendDTO : sendList) {
                    Long openCount = 0L;
                    if (temp.containsKey(sendDTO.getEmployeeId())) {
                        openCount = temp.get(sendDTO.getEmployeeId()).getOpenNum();
                    }
                    sendDTO.setOpenNum(openCount);
                    DecimalFormat df = new DecimalFormat("0.00%");
                    String openRate = df.format(sendDTO.getOpenNum() / (sendDTO.getPushNum() * 1.0));
                    sendDTO.setOpenRate(openRate);
                }
                return sendList;
            }
        }

        if (StringUtils.equals(dto.getQueryType(), "2")) {
            QueryWrapper<QueryPushDataStatisticDTO> queryShopWrapper = new QueryWrapper<>();
            queryShopWrapper.eq(StringUtils.isNotBlank(dto.getProductType()), "b.mode_id", dto.getProductType());
            queryShopWrapper.ge(dto.getStartTime() != null, "a.send_time", dto.getStartTime());
            queryShopWrapper.le(dto.getEndTime() != null, "a.send_time", dto.getEndTime());
            queryShopWrapper.groupBy(" a.shop_id,b.name");
            queryShopWrapper.orderByDesc(" count(a.shop_id) ");

            String shopLine="";
            if(dto.getShopId()==null){
                shopLine= orgService.listShopLine();
            }else{
                shopLine=dto.getShopId()+"";
            }
            queryShopWrapper.apply("b.org_id in ("+shopLine+")");

            List<PushDataStatisticDTO> shopSendList = this.midprodSendMapper.querySendByShop(queryShopWrapper);
            if (shopSendList.size() > 0) {
                queryShopWrapper.apply("a.send_id=c.send_id");
                List<PushDataStatisticDTO> shopOpenList = this.midprodSendMapper.queryOpenByShop(queryShopWrapper);
                Map<Long, PushDataStatisticDTO> temp = new HashMap<>();

                if (ArrayUtils.isNotEmpty(shopOpenList)) {
                    for (PushDataStatisticDTO statisticDTO : shopOpenList) {
                        temp.put(statisticDTO.getOrgId(), statisticDTO);
                    }
                }

                for (PushDataStatisticDTO sendDTO : shopSendList) {
                    Long openCount = 0L;
                    if (temp.containsKey(sendDTO.getOrgId())) {
                        openCount = temp.get(sendDTO.getOrgId()).getOpenNum();
                    }
                    sendDTO.setOpenNum(openCount);
                    DecimalFormat df = new DecimalFormat("0.00%");
                    if(sendDTO.getOpenNum().equals(0L)){
                        sendDTO.setOpenRate("0.00%");
                    }else{
                        String openRate = df.format(sendDTO.getOpenNum() / (sendDTO.getPushNum() * 1.0));
                        sendDTO.setOpenRate(openRate);
                    }

                }
                return shopSendList;
            }
        }
        return null;
    }

    @Override
    public List<PushDataStatisticDTO> queryTopData(QueryPushDataStatisticDTO dto) {
        QueryWrapper<QueryPushDataStatisticDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy(" b.title ");
        queryWrapper.orderByDesc(" count(b.title) ");
        String shopLine="";
        if(dto.getShopId()==null){
            shopLine= orgService.listShopLine();
        }else{
            shopLine=dto.getShopId()+"";
        }
        queryWrapper.apply("b.shop_id in ("+shopLine+")");

        if(StringUtils.equals(dto.getQueryType(),"1")){
            queryWrapper.ge(dto.getStartTime()!=null,"b.send_time",dto.getStartTime());
            queryWrapper.le(dto.getEndTime()!=null,"b.send_time",dto.getEndTime());
            List<PushDataStatisticDTO> list=this.midprodSendMapper.queryTopPush(queryWrapper);
            return list;
        }

        if(StringUtils.equals(dto.getQueryType(),"2")){
            queryWrapper.ge(dto.getStartTime()!=null,"b.open_time",dto.getStartTime());
            queryWrapper.le(dto.getEndTime()!=null,"b.open_time",dto.getEndTime());
            List<PushDataStatisticDTO> list=this.midprodSendMapper.queryTopOpen(queryWrapper);
            return list;
        }

        return null;
    }

}