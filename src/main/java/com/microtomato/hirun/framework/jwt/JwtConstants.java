package com.microtomato.hirun.framework.jwt;

/**
 * @author Steven
 * @date 2020-09-26
 */
public final class JwtConstants {

    public static final String JWT_SECRET_KEY = "hirun@tomato.com";
    public static final String HEAD_AUTHORIZATION = "Authorization";
    public static final String HEAD_AUTHORIZATION_BEARER = "Bearer ";
    public static final long EXPIRATION_ONE_DAY = 1000 * 60 * 60 * 24;

}
