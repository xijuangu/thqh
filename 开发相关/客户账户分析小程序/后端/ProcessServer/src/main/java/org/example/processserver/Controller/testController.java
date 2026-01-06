package org.example.processserver.Controller;

import com.opencsv.exceptions.CsvValidationException;
import org.example.processserver.Utils.receiveCSV;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * @author PY.Lu
 * @date 2024/10/30
 * @Description
 */
@RestController
public class testController {

    @Autowired
    private receiveCSV receiveCSV;

//    @GetMapping("/test")
    @Test
    public void test() throws IOException, CsvValidationException {
        String MainPath = "D:/MY/通惠期货APP/功能开发需求/CSV_file/接收CSV文件/数仓/";
        String fileName = "ads_futures_client_finance_status";
        String filePath = getFileTrueName(MainPath, fileName);
        if (filePath == null) {
            System.out.println("文件不存在");
        }
        System.out.println(filePath);
    }
    public String getFileTrueName(String MainPath, String fileName) {
        //遍历目录下所有CSV文件
        File file = new File(MainPath);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".csv")) {
                if (f.getName().contains(fileName)) {
                    return f.getName();
                }
            }
        }
        return null;
    }


}
