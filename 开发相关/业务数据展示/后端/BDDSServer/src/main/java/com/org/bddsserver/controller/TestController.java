package com.org.bddsserver.controller;

import com.org.bddsserver.exception.Result;
import com.org.bddsserver.pojo.TestPojo;
import com.org.bddsserver.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description
 */
@RestController
@Slf4j
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/database_connec_test")
    public Result<TestPojo> test2() {
        TestPojo testPojo = testService.getOneTestPojo();
        return Result.success(testPojo);
    }



}
