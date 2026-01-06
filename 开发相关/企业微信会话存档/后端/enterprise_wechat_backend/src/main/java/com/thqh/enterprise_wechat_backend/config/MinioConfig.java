package com.thqh.enterprise_wechat_backend.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MinioConfig
 * @Description:
 * @Author liubin
 * @Date 2025/3/11 14:17
 * @Version V1.0
 */
@Configuration
public class MinioConfig {

    // 从 application.properties 或 application.yml 中读取配置
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
