package com.org.bddsserver.service.impl;

import com.org.bddsserver.mapper.TestMapper;
import com.org.bddsserver.pojo.TestPojo;
import com.org.bddsserver.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public TestPojo getOneTestPojo() {
        return testMapper.getOneTestPojo_byMapperXML();
    }
}
