package org.example.processserver.Controller;

import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.processserver.Utils.*;
import org.example.processserver.mapper.db1.csvName_TypeMapper;
import org.example.processserver.pojo.ApiResponse;
import org.example.processserver.pojo.csvName_Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/10/29
 * @Description
 */
@RestController
@Slf4j
public class mainExecute {
    @Autowired
    private Process_Init processInit;
    @Autowired
    private Process_Incre processIncre;
    @Autowired
    private CheckAmountUtil checkAmount;
    @Autowired
    private buildCSV builder;
    @Autowired
    private receiveCSV receive_CSV;
    @Autowired
    private csvName_TypeMapper csvName_TypeM;

    //全量表数据加密处理
    @RequestMapping("/execute_processData")
    public ResponseEntity<?> execute_processData(@RequestParam("type") Integer type) throws Exception {
        log.info(" ====================== /execute_processData数据加密处理接口调用 ====================== ");

        List<String> tableNameList = new ArrayList<>();
        tableNameList.add("t_client_sett");
        tableNameList.add("t_close_detail");
        tableNameList.add("t_delivery");
        tableNameList.add("t_execute_result");
        tableNameList.add("t_hold_balance");
        tableNameList.add("t_fund_jour");
        tableNameList.add("t_entrust");
        tableNameList.add("t_done");
        tableNameList.add("t_hold_detail");

        int result = 0;
        String typeStr = "";
        String responseStr = "";
        if (type == 1) {
            typeStr = "初始全量";
            for (String tableName : tableNameList) {
                result = processInit.process_init(tableName);
            }
        } else if (type == 2) {
            typeStr = "增量";
            for (String tableName : tableNameList) {
                result = processIncre.main(tableName);
            }
        }
        if (result == 0) {
            responseStr = "所有" + typeStr + "数据加密任务处理完成";
            return ResponseEntity.ok(ApiResponse.success(responseStr));
        } else {
            responseStr = "部分" + typeStr + "数据加密任务处理失败，请检查日志";
            return ResponseEntity.ok(ApiResponse.fail(1001, responseStr));
        }

    }

    //指定表数据加密处理
    @RequestMapping("/execute_processData_customTable")
    public ResponseEntity<?> execute_customize_processData(@RequestParam("type") Integer type, @RequestParam("tableName") String tableName) throws Exception {
        int result = 0;
        String typeStr = "";
        String responseStr = "";
        if (type == 1) {
            typeStr = "初始全量";
            result = processInit.process_init(tableName);
        } else if (type == 2) {
            typeStr = "增量";
            result = processIncre.main(tableName);
        }
        if (result == 0) {
            responseStr = tableName + "表" + typeStr + "数据加密任务处理完成";
            return ResponseEntity.ok(ApiResponse.success(responseStr));
        } else {
            responseStr = tableName + "表" + typeStr + "数据加密任务处理失败，请检查日志";
            return ResponseEntity.ok(ApiResponse.fail(1002, responseStr));
        }
    }

    //全量表生成CSV文件
    @RequestMapping("/execute_generateData")
    public ResponseEntity<?> execute_generateCSV(@RequestParam("type") Integer type) throws Exception {
        if (type == 1) {
            log.info(" ====================== /execute_generateCSV文件生成接口调用，初始全量数据批量生成CSV文件 ====================== ");
        } else if (type == 2) {
            log.info(" ====================== /execute_generateCSV文件生成接口调用，增量数据批量生成CSV文件 ========== ");
        } else if (type == 3) {

        }

        //获取当前日期
        // 创建一个SimpleDateFormat对象，用于格式化日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // 将日期减去一天，得到昨天的日期
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        // 格式化昨天的日期
        String dateYesterday = formatter.format(yesterday);

        String tableName1 = "S_KINGSTAR_CLIENT_SETT";
        String tableName2 = "S_KINGSTAR_CLOSE_DETAIL";
        String tableName3 = "S_KINGSTAR_HOLD_BALANCE";
        String tableName4 = "S_KINGSTAR_DELIVERY";
        String tableName5 = "S_KINGSTAR_EXECUTE_RESULT";
        String tableName6 = "S_KINGSTAR_PRODUCT";
        String tableName7 = "S_KINGSTAR_PUB_DATE";

        builder.generateCSV(dateYesterday, dateYesterday, tableName1, type);
        builder.generateCSV(dateYesterday, dateYesterday, tableName2, type);
        builder.generateCSV(dateYesterday, dateYesterday, tableName3, type);
        builder.generateCSV(dateYesterday, dateYesterday, tableName4, type);
        builder.generateCSV(dateYesterday, dateYesterday, tableName5, type);
        builder.generateCSV(dateYesterday, dateYesterday, tableName6, type);
        builder.generateCSV(dateYesterday, dateYesterday, tableName7, type);

        String typeStr = "";
        if (type == 1) {
            typeStr = "全量";
        } else if (type == 2) {
            typeStr = "增量";
        }
        String responseStr = "已为所有表生成" + typeStr + "Data文件";

        return ResponseEntity.ok(ApiResponse.success(responseStr));
    }

    // 指定表生成CSV文件
    @RequestMapping("/execute_generateData_customTable")
    public ResponseEntity<?> execute_customize_generateCSV(@RequestParam("type") Integer type, @RequestParam("tableName") String tableName) throws Exception {
        if (type == 1) {
            log.info(" ====================== /execute_customize_generateCSV 文件生成接口调用，指定表生成初始数据CSV文件：{} ====================== ", tableName);
        } else if (type == 2) {
            log.info(" ====================== /execute_customize_generateCSV 文件生成接口调用，指定表生成增量数据CSV文件：{} ====================== ", tableName);
        }

        //获取当前日期
        // 创建一个SimpleDateFormat对象，用于格式化日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // 将日期减去一天，得到昨天的日期
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        // 格式化昨天的日期
        String dateYesterday = formatter.format(yesterday);

        builder.generateCSV(dateYesterday, dateYesterday, tableName, type);

        log.info(" --------------------------------------------------------------------------------------------------------- ");
        String typeStr = "";
        if (type == 1) {
            typeStr = "全量";
        } else if (type == 2) {
            typeStr = "增量";
        }
        String responseStr = "已为" + tableName + "表生成" + typeStr + "Data文件";

        return ResponseEntity.ok(ApiResponse.success(responseStr));
    }


    @GetMapping("/generatecheckJson")
    public String generateCheckJson(@RequestParam("type") Integer type) throws Exception {
        builder.generateCheckJson(type);
        return "已生成checkJson文件";
    }


    //处理指定日期接收的CSV文件
    @GetMapping("/execute_dataPersistence_customDate")
    public ResponseEntity<?> receiveProcess_assignDate(@RequestParam("assignDate") String assigDate) throws CsvValidationException, IOException {
        log.info(" ====================== /receiveCSV_date 接收文件数据处理接口调用，指定日期：{} ========== ", assigDate);

        List<csvName_Type> csv_name_type_list = csvName_TypeM.getCsvName_Type_all();
        String result = new String();
        List<String> resultList = new ArrayList<>();
        for (csvName_Type c : csv_name_type_list) {
            result = assigDate + "日" + receive_CSV.readCSV_summary(c.getCsvName(), c.getAlias(), assigDate);
            resultList.add(result);
            log.info(" --------------------------------------------------------------------------------------------------------- ");
        }
        return ResponseEntity.ok(ApiResponse.success(resultList.toString()));
    }

    //处理所有接收的CSV文件
    @GetMapping("/execute_dataPersistence")
    public ResponseEntity<?> receiveProcess() throws CsvValidationException, IOException {
        log.info(" ====================== /receiveCSV 接收文件数据处理接口调用 ====================== ");

        List<csvName_Type> csv_name_type_list = csvName_TypeM.getCsvName_Type_all();
        String result = new String();
        List<String> resultList = new ArrayList<>();
        for (csvName_Type c : csv_name_type_list) {
            result = receive_CSV.readCSV_summary(c.getCsvName(), c.getAlias(), "");

            resultList.add(result);
            log.info(" --------------------------------------------------------------------------------------------------------- ");
        }

        return ResponseEntity.ok(ApiResponse.success(resultList.toString()));
    }

    //数据同步数量一致性检查
    @GetMapping("/checkAmount")
    public ResponseEntity<?> checkAmount(@RequestParam("type") Integer type) throws Exception {
        log.info(" ====================== 数据同步数量一致性检查接口调用 ========== ");
        int i = checkAmount.CheckAmount(type);
        String responseStr = "同步数据量检查完成，thqh数据库SyncStateTable表已更新，请查看该表检查同步情况";
        if (i == 1) {
            responseStr = "全量" + responseStr;
        } else if (i == 2) {
            responseStr = "增量" + responseStr;
        }
        return ResponseEntity.ok(ApiResponse.success(responseStr));
    }


}
