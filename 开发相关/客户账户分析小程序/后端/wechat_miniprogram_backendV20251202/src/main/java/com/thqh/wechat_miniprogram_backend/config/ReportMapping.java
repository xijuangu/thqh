package com.thqh.wechat_miniprogram_backend.config;

import lombok.Data;

/**
 * @ClassName: ReportMapping
 * @Description:存储每个 reportCode 对应的报表路径及具体参数
 * @Author liubin
 * @Date 2025/1/9 17:23
 * @Version V1.0
 */
@Data
public class ReportMapping {
    private String path;
    private String parameter_client_id;
}
