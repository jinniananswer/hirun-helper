package com.microtomato.hirun.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 登录后的用户上下文
 *
 * @author Steven
 * @date 2019-09-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContext implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 Id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 拥有的角色集
     */
    private List<Role> roles;

    /**
     * 是否为超级管理员，ins_role.role_id = 1
     */
    private boolean admin;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 组织 Id
     */
    private Long orgId;

    /**
     * 雇员 Id
     */
    private Long employeeId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 主角色，每个用户最多有一个主角色
     */
    @Builder.Default
    private Long mainRoleId = -1L;

    /**
     * 登录时间
     */
    @Builder.Default
    private LocalDateTime loginTime = LocalDateTime.now();

    /**
     * 权限，角色
     */
    private Collection<GrantedAuthority> grantedAuthorities;

    /**
     * 账户是否没过期
     *
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否没被锁定
     *
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账户密码是否没过期
     *
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否可用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return "0".equals(status);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

}
