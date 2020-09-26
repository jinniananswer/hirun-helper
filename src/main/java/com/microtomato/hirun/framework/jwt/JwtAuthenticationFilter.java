package com.microtomato.hirun.framework.jwt;


import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


/**
 * token 的校验
 * 该类继承自 BasicAuthenticationFilter，在 doFilterInternal 方法中，
 * 从 http 头的 Authorization 项读取 token 数据，然后用 Jwts 包提供的方法校验 token 的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求

 *
 * @author Steven
 * @date 2020-09-26
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtConstants.KeyAuthorization);

        if (null == header || !header.startsWith(JwtConstants.HEAD_AUTHORIZATION_BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (null != token) {
            // parse the token.
            String user = Jwts.parser()
                .setSigningKey(JwtConstants.jwtSecretKey)
                .parseClaimsJws(token.replace(JwtConstants.HEAD_AUTHORIZATION_BEARER, ""))
                .getBody()
                .getSubject();

            if (null != user) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }


}
