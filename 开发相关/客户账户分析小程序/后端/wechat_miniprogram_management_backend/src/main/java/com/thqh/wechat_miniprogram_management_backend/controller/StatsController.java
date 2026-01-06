package com.thqh.wechat_miniprogram_management_backend.controller;

import com.thqh.wechat_miniprogram_management_backend.dto.*;
import com.thqh.wechat_miniprogram_management_backend.service.StatsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName: StatsController
 * @Description:
 * @Author liubin
 * @Date 2025/11/4 16:10
 * @Version V1.0
 */

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * 用户统计接口（序号 1~7）
     */
    @PostMapping("/users")
    public ResponseEntity<?> getUserStats(@Validated @RequestBody UserStatsRequest request) {
        UserStatsResponse result = statsService.getUserStats(request);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 功能使用统计接口（序号 8~11）
     */
    @PostMapping("/functions")
    public ResponseEntity<?> getFunctionUsage(@Validated @RequestBody FunctionUsageRequest request) {
        FunctionUsageResponse result = statsService.getFunctionUsage(request);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}


