package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 认证结果
 *
 * @author Steven
 * @date 2020-10-03
 */
@Getter
@Setter
@ToString
public class AuthResultDTO {

    /**
     * jwt令牌
     */
    private String jwt;

    /**
     * 功能集合
     */
    private List<String> funcCodes;

}
