package com.thqh.wechat_miniprogram_backend.controller;

import com.thqh.wechat_miniprogram_backend.dto.ApiResponse;
import com.thqh.wechat_miniprogram_backend.dto.ReportRequest;
import com.thqh.wechat_miniprogram_backend.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ReportController
 * @Description:
 * @Author liubin
 * @Date 2025/1/8 16:34
 * @Version V1.0
 */


@RestController
@RequestMapping("/api-wx/reports")
@Validated
public class ReportController {

    private final ReportService reportService;
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);


    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * POST /api/reports
     * 请求体:
     * {
     * "reportCode": "REPORT123"
     * }
     * 成功响应:
     * {
     * "code": 0,
     * "msg": "",
     * "data": "https://bi.example.com/report?report_code=REPORT123&user_id=encryptedUserId&ssotoken=encryptedSsotoken"
     * }
     * <p>
     * 失败响应:
     * {
     * "code": 1004,
     * "msg": "未绑定交易账号",
     * "data": null
     * }
     */
    @PostMapping
    public ResponseEntity<?> getReportUrl(@Valid @RequestBody ReportRequest request) {
        logger.info("接收到报表访问请求，报表代码: {}", request.getReportCode());
        // 生成报表访问地址
        String reportUrl = reportService.generateReportUrl(request.getReportCode());
        Map<String, Object> result = new HashMap<>();
        result.put("report_url", reportUrl);
        logger.info("报表访问请求响应内容: {}", ApiResponse.success(result));
        // 返回响应
        return ResponseEntity.ok(ApiResponse.success(result));
    }

}

