package com.thqh.wechat_miniprogram_backend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: GetAuthRequest
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 14:15
 * @Version V1.0
 */

@Data
public class GetPhoneNumberRequest {

    @NotBlank(message = "code不能为空")
    private String code;
}