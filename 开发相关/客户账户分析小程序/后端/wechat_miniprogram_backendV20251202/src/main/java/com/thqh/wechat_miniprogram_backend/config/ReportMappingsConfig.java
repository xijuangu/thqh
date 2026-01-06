package com.thqh.wechat_miniprogram_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: ReportMappingsConfig
 * @Description:加载报表代码与报表路径及具体参数的映射配置
 * @Author liubin
 * @Date 2025/1/9 15:52
 * @Version V1.0
 */
@Component
@ConfigurationProperties(prefix = "report")
@Data
public class ReportMappingsConfig {
    private Map<String, ReportMapping> mappings;
}
