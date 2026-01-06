package org.example.processserver;

import org.example.processserver.Utils.CheckAmountUtil;
import org.example.processserver.Utils.SM4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author PY.Lu
 * @date 2024/11/20
 * @Description
 */
@RestController
public class test {
    @Autowired
    private CheckAmountUtil checkAmountUtil;

    @RequestMapping("/test")
    public String  test() throws Exception {
        String clientid = "0d5c811cbbb6b77ed172f507af53e7d4";
        String decryptText = SM4Util.decryptTextCBC(clientid);
        return decryptText;
    }
}
