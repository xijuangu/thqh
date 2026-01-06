package com.thqh.enterprise_wechat_backend.config;

import com.thqh.enterprise_wechat_backend.job.DownloadChatMessageJob;
import com.thqh.enterprise_wechat_backend.job.DownloadPersonnelJob;
import com.thqh.enterprise_wechat_backend.util.AutowiringSpringBeanJobFactory;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: QuartzConfig
 * @Description:
 * @Author liubin
 * @Date 2025/3/17 11:12
 * @Version V1.0
 */
@Configuration
public class QuartzConfig {

    private final DataSource dataSource;
    private final AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory;

    public QuartzConfig(DataSource dataSource, AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory) {
        this.dataSource = dataSource;
        this.autowiringSpringBeanJobFactory = autowiringSpringBeanJobFactory;
    }


    /**
     * 配置 JobDetail
     */
    // 定义任务详情--定时下载聊天记录
    @Bean
    public JobDetail downloadChatMessageJobDetail() {
        return JobBuilder.newJob(DownloadChatMessageJob.class)
                // 任务名称
                .withIdentity("downloadChatMessageJob")
                // 即使没有 Trigger 关联也能保留
                .storeDurably()
                .build();
    }

    // 定义任务详情--定时下载人员列表
    @Bean
    public JobDetail downloadPersonnelJobDetail() {
        return JobBuilder.newJob(DownloadPersonnelJob.class)
                // 任务名称
                .withIdentity("downloadPersonnelJob")
                // 即使没有 Trigger 关联也能保留
                .storeDurably()
                .build();
    }

    // 定义触发器--定时下载聊天记录
    @Bean
    public CronTrigger downloadChatMessageJobTrigger() {

        String cronExpression = "0 */30 * * * ?";//每隔半小时

        return TriggerBuilder.newTrigger()
                .forJob(downloadChatMessageJobDetail())
                .withIdentity("downloadChatMessageJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing())
                .build();
    }

    // 定义触发器--定时下载人员列表
    @Bean
    public CronTrigger downloadPersonnelJobTrigger() {

        String cronExpression = "0 */30 * * * ?";//每隔半小时

        return TriggerBuilder.newTrigger()
                .forJob(downloadPersonnelJobDetail())
                .withIdentity("downloadPersonnelJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing())
                .build();
    }


//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(Trigger downloadChatMessageJobTrigger, Trigger downloadPersonnelJobTrigger, JobDetail downloadChatMessageJobDetail, JobDetail downloadPersonnelJobDetail) {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setTriggers(downloadChatMessageJobTrigger, downloadPersonnelJobTrigger);
//        factory.setJobDetails(downloadChatMessageJobDetail, downloadPersonnelJobDetail);
//
//        // 设置数据源
//        factory.setDataSource(dataSource);
//
//        // 使用自定义的AutowiringSpringBeanJobFactory
//        factory.setJobFactory(autowiringSpringBeanJobFactory);
//
//        return factory;
//    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger downloadChatMessageJobTrigger, JobDetail downloadChatMessageJobDetail) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setTriggers(downloadChatMessageJobTrigger);
        factory.setJobDetails(downloadChatMessageJobDetail);


        // 设置数据源
        factory.setDataSource(dataSource);

        // 使用自定义的AutowiringSpringBeanJobFactory
        factory.setJobFactory(autowiringSpringBeanJobFactory);

        return factory;
    }
}
