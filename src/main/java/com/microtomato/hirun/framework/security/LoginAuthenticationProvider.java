package com.microtomato.hirun.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Steven
 * @date 2019-12-20
 */
@Slf4j
@Component
public class LoginAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private void setJdbcUserDetailsService() {
        setUserDetailsService(customUserDetailsService);
    }

    @PostConstruct
    public void initProvider() {
        ReloadableResourceBundleMessageSource localMessageSource = new ReloadableResourceBundleMessageSource();
        localMessageSource.setBasenames("messages_zh_CN");
        messages = new MessageSourceAccessor(localMessageSource);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        Object credentials = authentication.getCredentials();
        if (null == credentials) {
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "密码不正确！"));
        }

        String presentedPassword = credentials.toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "密码不正确！"));
        }
    }

}
