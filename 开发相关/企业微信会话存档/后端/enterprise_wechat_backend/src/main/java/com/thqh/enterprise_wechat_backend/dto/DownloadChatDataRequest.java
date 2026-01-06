package com.thqh.enterprise_wechat_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DownloadChatDataRequest
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:50
 * @Version V1.0
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DownloadChatDataRequest {
    // 默认从 seq=0 开始
    @Builder.Default
    private long seq = 0;
    // 默认拉取最大1000条消息
    @Builder.Default
    private int limit = 1000;
    // 代理默认空
    @Builder.Default
    private String proxy = "";
    // 密码默认空
    @Builder.Default
    private String password = "";
    // 默认超时时间30秒
    @Builder.Default
    private int timeout = 30;
    // MANUAL_MODE:手动模式取传参的参数seq的值和limit等，其它情况一律默认值：seq取表中最大seq值
    @Builder.Default
    private String type = "AUTO_MODE";
}