package com.thqh.enterprise_wechat_backend.controller;

import com.thqh.enterprise_wechat_backend.dto.DownloadChatDataRequest;
import com.thqh.enterprise_wechat_backend.service.WeChatService;
import com.thqh.enterprise_wechat_backend.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: WeChatController
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:18
 * @Version V1.0
 */


@RestController

@RequestMapping("/api/wechat")
@CrossOrigin(origins = "*", maxAge = 3600)//开发阶段允许跨域请求
public class WeChatController {
    private final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    private final WeChatService weChatService;

    public WeChatController(WeChatService weChatService) {
        this.weChatService = weChatService;
    }

    @PostMapping("/chatdata/download")
    public ResponseEntity<ApiResponse<String>> downloadChatData(@RequestBody(required = false) DownloadChatDataRequest downloadChatDataRequest) {
        if (downloadChatDataRequest == null) {
            // 触发默认值
            downloadChatDataRequest = DownloadChatDataRequest.builder().build();
        }
        // 调用服务层获取会话内容存档
        logger.info("获取会话内容存档请求参数: {}", downloadChatDataRequest);
        weChatService.getChatData(downloadChatDataRequest);
        logger.info("获取会话内容存档响应内容: {}", ApiResponse.success(null));
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PostMapping("/user/download")
    public ResponseEntity<ApiResponse<String>> getWechatUser() {
        weChatService.getWechatUser();
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/customer/download")
    public ResponseEntity<ApiResponse<String>> getCustomer() {
        weChatService.getCustomer();
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/groupchat/download")
    public ResponseEntity<ApiResponse<String>> getGroupChat() {
        weChatService.getGroupChat();
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
