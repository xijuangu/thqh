package com.thqh.enterprise_wechat_backend.job;

import com.thqh.enterprise_wechat_backend.dto.ApiResponse;
import com.thqh.enterprise_wechat_backend.dto.DownloadChatDataRequest;
import com.thqh.enterprise_wechat_backend.service.WeChatService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName: DownloadPersonnelJob
 * @Description:
 * @Author lupeiying
 * @Date 2025/5/13 14:22
 * @Version V1.0
 */
@DisallowConcurrentExecution
@Component
public class DownloadPersonnelJob  implements Job {

    private final Logger logger = LoggerFactory.getLogger(DownloadPersonnelJob.class);

    private WeChatService weChatService;

    @Autowired
    public void setWeChatService(WeChatService weChatService) {
        this.weChatService = weChatService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {

            logger.info("开始执行企微客户信息下载任务...");
            weChatService.getCustomer();
            logger.info("企微客户信息下载任务执行完毕...");

            logger.info("开始执行企微群聊信息下载任务...");
            weChatService.getGroupChat();
            logger.info("企微群聊信息下载任务执行完毕...");

        } catch (Exception e) {
            logger.error("Error executing DownloadChatMessageJob", e);
            throw new JobExecutionException(e);
        }
    }
}