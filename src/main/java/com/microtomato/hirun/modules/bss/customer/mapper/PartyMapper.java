package com.microtomato.hirun.modules.bss.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustomerInfoDetailDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.Party;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface PartyMapper extends BaseMapper<Party> {
    @Select("select a.party_name,a.mobile_no,c.link_employee_id,a.consult_time as consult_time, a.party_id,a.wx_nick,a.open_id, " +
            " b.house_mode,b.house_area,b.house_address," +
            " b.house_id,d.house_counselor_id,d.cust_name as customer_name ,d.cust_id,d.house_mode as counselor_house_mode,d.house_detail,d.mobile_no as counselor_mobile_no " +
            " from ins_project b ,ins_project_linkman c ,ins_party a left join ins_customer d on (a.open_id= d.identify_code)" +
            " ${ew.customSqlSegment}"
    )
    IPage<CustomerInfoDetailDTO> queryCustomerInfo(Page<CustQueryCondDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询客户代表环节录入的客户信息
     * @param partyId
     * @return
     */
    @Select(" select a.party_name as customer_name,a.age,a.sex,a.wx_nick,a.mobile_no,a.educational,a.family_members_count," +
            " a.hobby,a.other_hobby,a.create_time as consult_time,a.head_url, " +
            " b.house_mode, b.house_area,b.house_address as house_detail,b.information_source,c.link_employee_id , " +
            " b.house_id as house_id,b.house_building,b.house_room_no " +
            " from ins_party a ,ins_project b ,ins_project_linkman c " +
            " where a.party_id=b.party_id " +
            " and a.party_status='0'" +
            " and b.project_id=c.project_id " +
            " and c.role_type='CUSTOMERSERVICE' " +
            " and a.party_id=#{partyId} "
    )
    CustomerInfoDetailDTO queryPartyInfoDetail(Long partyId);
}
