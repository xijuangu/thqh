package com.thqh.enterprise_wechat_backend.job;

import com.thqh.enterprise_wechat_backend.dto.DownloadChatDataRequest;
import com.thqh.enterprise_wechat_backend.service.WeChatService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: DownloadChatMessageJob
 * @Description:
 * @Author liubin
 * @Date 2025/3/17 11:11
 * @Version V1.0
 */
@DisallowConcurrentExecution
@Component
public class DownloadChatMessageJob  implements Job {

    private final Logger logger = LoggerFactory.getLogger(DownloadChatMessageJob.class);

    private WeChatService weChatService;

    @Autowired
    public void setWeChatService(WeChatService weChatService) {
        this.weChatService = weChatService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            DownloadChatDataRequest downloadChatDataRequest = new DownloadChatDataRequest();
            weChatService.getChatData(downloadChatDataRequest);
        } catch (Exception e) {
            logger.error("Error executing DownloadChatMessageJob", e);
            throw new JobExecutionException(e);
        }
    }
}