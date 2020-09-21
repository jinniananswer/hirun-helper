package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工提成明细表数据对象
 * @author: jinnian
 * @create: 2020-06-14 14:24
 **/
@Data
public class SalaryRoyaltyDetailDTO {

    /**
     * 设计提成明细
     */
    private List<DesignRoyaltyDetailDTO> designRoyaltyDetails;

    /**
     * 工程提成明细
     */
    private List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails;
}
