package com.logdemo.lightserver.Controller;

import com.logdemo.lightserver.Mapper.dataMapper;
import com.logdemo.lightserver.pojo.data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * Excel文件上传控制器
 * 功能：接收前端上传的Excel文件，解析其中的数据并保存到数据库
 */
@RestController
@CrossOrigin(origins = "http://localhost:8083") //开发环境允许来自8083端口的跨域请求
@Slf4j
public class uploadController {

    @Autowired
    private dataMapper repository; // Replace lswith your repository

    /**
     * Excel文件上传接口
     *
     * @param file 前端上传的Excel文件（MultipartFile类型）
     * @return ResponseEntity<?> 统一的HTTP响应结果
     */
    @PostMapping("/api_lcidataupload/load")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("上传Excel文件为空！");
        }

        try {
            // 2. 读取Excel文件内容，解析为数据列表
            List<data> dataList = readExcelFile(file);
            // 3. 将解析后的数据保存到数据库
            saveToDatabase(dataList);
            // 4. 返回成功响应
            return ResponseEntity.ok("Data uploaded successfully");
        } catch (IllegalArgumentException e) {
            // 处理参数异常（如日期格式错误）
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            // 处理IO异常（如文件读取失败）
            return ResponseEntity.badRequest().body("发生未知异常，请联系管理员: " + e.getMessage());
        }
    }

    /**
     * 读取Excel文件内容并解析为数据列表
     *
     * @param file 上传的Excel文件
     * @return List<data> 解析后的数据对象列表
     * @throws IOException 文件读取IO异常
     */
    private List<data> readExcelFile(MultipartFile file) throws IOException {
        // 初始化数据列表，用于存储解析后的Excel数据
        List<data> dataList = new ArrayList<>();

        // 使用try-with-resources语法自动关闭Workbook资源，避免内存泄漏
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // 获取Excel第一个工作表（索引从0开始）
            Sheet sheet = workbook.getSheetAt(0);

            // 获取工作表最后一行的行号
            int lastRowNum = sheet.getLastRowNum();

            // 跳过表头行（i从1开始），遍历所有数据行
            for (int i = 1; i <= lastRowNum; i++) {
                // 获取当前行对象
                Row row = sheet.getRow(i);
                // 如果当前行为空，终止循环
                if (row == null) break;
                // 创建数据对象，用于存储当前行的解析结果
                data data = new data();

                // ========== 解析第一列：日期（必填项） ==========
                String dateStr = getStringCellValue(row.getCell(0));
                if (dateStr == null) {
                    // 记录错误日志
                    log.error("Date is required at row " + (i + 1));
                    // 抛出异常，终止文件解析
                    throw new IllegalArgumentException("Date is required at row " + (i + 1));
                }

                // 格式化日期为统一格式（yyyy-MM-dd）
                data.setDate(formatDate(dateStr));

                // ========== 解析其他数值列 ==========
                data.setSpodumene(getDoubleCellValue(row.getCell(1))); // 锂辉石
                data.setLg_Lepidolite(getDoubleCellValue(row.getCell(2))); // 低品位锂云母
                data.setHg_Lepidolite(getDoubleCellValue(row.getCell(3))); // 高品位锂云母
                data.setLFP_Cathode(getDoubleCellValue(row.getCell(4))); // 磷酸铁锂正极片
                data.setLFP_BattPower(getDoubleCellValue(row.getCell(5))); // 磷酸铁锂电池粉
                data.setNCM(getDoubleCellValue(row.getCell(6))); // 三元极片粉
                data.setEC_Spot(getDoubleCellValue(row.getCell(7))); // 电碳现货价
                data.setComposite_Index(getDoubleCellValue(row.getCell(8))); // 综合指数

                // 将解析后的行数据添加到列表
                dataList.add(data);
            }
        }

        return dataList;
    }

    /**
     * 获取单元格的字符串值（兼容不同单元格类型）
     *
     * @param cell Excel单元格对象
     * @return String 单元格的字符串值，空单元格返回null
     */
    private String getStringCellValue(Cell cell) {
        // 单元格为空，返回null
        if (cell == null) return null;

        // 根据单元格类型解析值（修复POI版本兼容问题，使用getCellTypeEnum()）
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue(); // 字符串类型，直接返回字符串值

            case NUMERIC: // 数值类型，区分日期和普通数字
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日期类型：转换为LocalDate并返回yyyy-MM-dd格式字符串
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();//无法解析 'Cell' 中的方法 'getLocalDateTimeCellValue'
                }
                // 普通数字：转换为字符串返回
                return String.valueOf(cell.getNumericCellValue());
            default:
                // 其他类型（如布尔、公式等）返回null
                return null;
        }
    }

    /**
     * 获取单元格的Double数值（兼容不同单元格类型和NA值）
     *
     * @param cell Excel单元格对象
     * @return Double 单元格的数值，空值/NA返回null
     */
    private Double getDoubleCellValue(Cell cell) {

        if (cell == null || cell.getCellType() == CellType.BLANK)
            return null;//运算符 '==' 不能应用于 'int'、'org.apache.poi.ss.usermodel.CellType'
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("NA")) {//运算符 '==' 不能应用于 'int'、'org.apache.poi.ss.usermodel.CellType'
            return null;
        }
        return cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : null;//运算符 '==' 不能应用于 'int'、'org.apache.poi.ss.usermodel.CellType'
    }

    /**
     * 日期格式化器列表：支持的日期格式
     * 1. yyyy-MM-dd（如：2025-01-01）
     * 2. yyyy/M/d（如：2025/1/1）
     */
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/M/d")
    );

    /**
     * 格式化日期字符串为统一的yyyy-MM-dd格式
     *
     * @param dateStr 原始日期字符串
     * @return String 格式化后的日期字符串
     * @throws IllegalArgumentException 日期格式不支持时抛出异常
     */
    public String formatDate(String dateStr) {
        // 遍历所有支持的格式化器，尝试解析日期
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                // 去除字符串首尾空格后解析
                LocalDate date = LocalDate.parse(dateStr.trim(), formatter);
                // 返回标准格式的日期字符串（yyyy-MM-dd）
                return date.toString(); // Returns in yyyy-MM-dd format
            } catch (Exception e) {
                // 解析失败，尝试下一个格式化器
                continue;
            }
        }
        // 所有格式化器都解析失败，记录错误日志并抛出异常
        System.err.println("Failed to parse date: " + dateStr);
        log.error("Failed to parse date: " + dateStr);
        log.error("Please check if the date format is yyyy-MM-dd or yyyy/MM/dd");
        throw new IllegalArgumentException("Excel中存在错误的日期格式. 请检查是否符合格式 yyyy-MM-dd 或 yyyy/MM/dd");
    }

    /**
     * 将解析后的数据保存到数据库
     * 逻辑：先清空数据表，再批量插入新数据（覆写模式）
     *
     * @param dataList 待保存的数据列表
     */
    private void saveToDatabase(List<data> dataList) {
        // 1. 清空数据表（覆写模式）
        repository.truncate_data();
        // 2. 遍历数据列表，逐条插入数据库
        for (data dto : dataList) {
            // 创建数据库实体对象 pojo
            data entity = new data(); // Replace with your entity class

            // 复制所有字段值
            entity.setDate(dto.getDate());
            entity.setSpodumene(dto.getSpodumene());
            entity.setLg_Lepidolite(dto.getLg_Lepidolite());
            entity.setHg_Lepidolite(dto.getHg_Lepidolite());
            entity.setLFP_Cathode(dto.getLFP_Cathode());
            entity.setLFP_BattPower(dto.getLFP_BattPower());
            entity.setNCM(dto.getNCM());
            entity.setEC_Spot(dto.getEC_Spot());
            entity.setComposite_Index(dto.getComposite_Index());
            // 保存单条数据到数据库
            int save = repository.save(entity);
        }
    }
}