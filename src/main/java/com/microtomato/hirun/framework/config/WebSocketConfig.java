package com.microtomato.hirun.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 自动注册所有 @ServerEndpoint 注解声明的 Websocket Endpoint
 *
 * @author Steven
 * @date 2019-11-02
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
