package com.thqh.wechat_miniprogram_backend.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName: GetAuthRequest
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 14:15
 * @Version V1.0
 */

@Data
public class GetAuthRequest {

    @NotBlank(message = "type不能为空")
    private String type;

    @NotBlank(message = "code不能为空")
    private String code;
}