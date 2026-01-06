
package com.thqh.wechat_miniprogram_backend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: ReportRequest
 * @Description:
 * @Author liubin
 * @Date 2025/1/8 16:39
 * @Version V1.0
 */
@Data
public class ReportRequest {
    @NotBlank(message = "报表代码不能为空")
    private String reportCode;
}
