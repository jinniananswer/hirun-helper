package com.microtomato.hirun.modules.bss.customer.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_party")
public class Party extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户编码
     */
    @TableId(value = "party_id", type = IdType.AUTO)
    private Long partyId;

    /**
     * 关键识别号
     */
    @TableField("open_id")
    private String openId;

    /**
     * 客户名称
     */
    @TableField("party_name")
    private String partyName;

    @TableField("sex")
    private String sex;

    @TableField("head_url")
    private String headUrl;

    /**
     * 年龄
     */
    @TableField("age")
    private String age;

    /**
     * 微信昵称
     */
    @TableField("wx_nick")
    private String wxNick;

    /**
     * 客户状态
     */
    @TableField("party_status")
    private Integer partyStatus;

    /**
     * 电话号码
     */
    @TableField("mobile_no")
    private String mobileNo;

    /**
     * QQ号码
     */
    @TableField("qq_no")
    private String qqNo;

    /**
     * 微信号码
     */
    @TableField("wx_no")
    private String wxNo;

    /**
     * 工作单位
     */
    @TableField("company")
    private String company;

    /**
     * 学历
     */
    @TableField("educational")
    private String educational;

    /**
     * 家人数量
     */
    @TableField("family_members_count")
    private String familyMembersCount;

    /**
     * 老男人个数
     */
    @TableField("oldman_count")
    private String oldmanCount;

    /**
     * 老女人个数
     */
    @TableField("oldwoman_count")
    private String oldwomanCount;

    /**
     * 老人详情
     */
    @TableField("older_detail")
    private String olderDetail;

    /**
     * 小男孩个数
     */
    @TableField("boy_count")
    private String boyCount;

    /**
     * 小女孩个数
     */
    @TableField("girl_count")
    private String girlCount;

    /**
     * 小孩详情
     */
    @TableField("child_detail")
    private String childDetail;

    /**
     * 兴趣爱好
     */
    @TableField("hobby")
    private String hobby;

    /**
     * 其他兴趣爱好
     */
    @TableField("other_hobby")
    private String otherHobby;

    /**
     * 咨询时间
     */
    @TableField("consult_time")
    private String consultTime;

    /**
     * 客户类型
     */
    @TableField("cust_type")
    private String custType;

    /**
     * 活动类型
     */
    @TableField("ploy_type")
    private String ployType;

    /**
     * 活动名称
     */
    @TableField("ploy_name")
    private String ployName;

    /**
     * 活动时间
     */
    @TableField("ploy_time")
    private LocalDate ployTime;

    /**
     * 电话咨询时间
     */
    @TableField("tel_consult_time")
    private LocalDateTime telConsultTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建员工
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改员工
     */
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
