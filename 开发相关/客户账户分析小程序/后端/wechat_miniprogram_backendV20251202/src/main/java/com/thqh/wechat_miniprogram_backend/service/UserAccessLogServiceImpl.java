package com.thqh.wechat_miniprogram_backend.service;

import com.thqh.wechat_miniprogram_backend.entity.UserAccessLog;
import com.thqh.wechat_miniprogram_backend.mapper.UserAccessLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
*@ClassName: UserAccessLogServiceImpl
*@Description:
*@Author 
*@Date 2025/10/23 15:59
*@Version V1.0
*/

@Service
public class UserAccessLogServiceImpl implements UserAccessLogService {

    private final UserAccessLogMapper userAccessLogMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserAccessLogServiceImpl.class);


    public UserAccessLogServiceImpl(UserAccessLogMapper userAccessLogMapper) {
        this.userAccessLogMapper = userAccessLogMapper;
    }

    @Override
    public void save(UserAccessLog logEntity) {
        try {
            userAccessLogMapper.insert(logEntity);
        } catch (Exception e) {
            logger.error("【用户访问日志保存失败】userId={}, functionCode={}, 异常={}",
                    logEntity.getUserId(), logEntity.getFunctionCode(), e.getMessage(), e);
        }
    }

}
