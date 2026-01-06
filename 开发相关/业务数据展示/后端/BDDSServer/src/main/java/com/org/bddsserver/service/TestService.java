package com.org.bddsserver.service;

import com.org.bddsserver.pojo.TestPojo;
import org.springframework.stereotype.Service;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description
 */

public interface TestService {
    /**
     * 测试获取一条客户类信息
     */
    TestPojo getOneTestPojo();
}
