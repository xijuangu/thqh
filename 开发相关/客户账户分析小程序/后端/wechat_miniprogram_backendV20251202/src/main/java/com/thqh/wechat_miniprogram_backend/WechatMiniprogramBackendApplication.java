package com.thqh.wechat_miniprogram_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.thqh.wechat_miniprogram_backend.config.ReportMappingsConfig;


@SpringBootApplication
@MapperScan("com.thqh.wechat_miniprogram_backend.mapper")
@EnableConfigurationProperties({ReportMappingsConfig.class})
public class WechatMiniprogramBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMiniprogramBackendApplication.class, args);
    }

}
