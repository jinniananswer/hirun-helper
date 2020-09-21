package com.microtomato.hirun.framework.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Steven
 * @date 2020-08-31
 */
@Slf4j
@Configuration
public class MinioConfig {

    @Value("${hirun.minio.endpoint}")
    private String endpoint;

    @Value("${hirun.minio.accessKey}")
    private String accessKey;

    @Value("${hirun.minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        MinioClient minioClient = new MinioClient(endpoint, accessKey, secretKey);
        log.info("初始化 MinioClient, endpoint: {}", endpoint);
        return minioClient;
    }

}
