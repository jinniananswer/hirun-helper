package com.microtomato.hirun.framework.jwt;

/**
 * @author Steven
 * @date 2020-09-26
 */
public final class JwtConstants {

    public static final String jwtSecretKey = "hirun@tomato.com";
    public static final String KeyAuthorization = "Authorization";
    public static final String HEAD_AUTHORIZATION_BEARER = "Bearer ";
    public static final long expiration = 1000 * 60 * 60 * 24;

}
