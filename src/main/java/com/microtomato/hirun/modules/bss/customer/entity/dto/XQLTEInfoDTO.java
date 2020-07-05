package com.microtomato.hirun.modules.bss.customer.entity.dto;

import com.microtomato.hirun.framework.data.TreeNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: hirun-helper
 * @description: 需求蓝图二信息传输对象
 * @author: liuhui
 **/
@Data
public class XQLTEInfoDTO {

    private List<StyleInfoDTO> styleInfo;

    private List<String> styleUrl;

    private Map<String,List<TreeNode>> funcInfo;
}
