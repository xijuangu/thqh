package com.org.bddsserver.utils;

import com.org.bddsserver.dto.UploadFileData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description
 */

@Component
public class FileParserUtil {
    private static final String[] EXPECTED_HEADERS = {
            "级别代码", "级别名称", "子类代码", "子类名称", "备注", "所属业务人员"
    };

    /**
     * TODO: 解析上传的文件（支持xls、csv）
     * @Param
     * @Param
     * @Return
     */
    public List<UploadFileData> parseFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null){
            throw new IllegalArgumentException("文件名不能为空");
        }

        if (fileName.endsWith(".xlsx")) {
            List<UploadFileData> list = parseExcelFile(file);
            if (list.isEmpty() || list.size() == 0) {
                throw new IllegalArgumentException("文件数据为空（可能仅包含表头），请补充客户类信息数据后再尝试上传。");
            }
            return list;
//        }else if (fileName.endsWith(".csv")) {
////            return parseCsvFile(file);
        }else {
            throw new IllegalArgumentException("不支持的文件格式，仅支持.xlsx和.csv文件");
        }
    }

    /**
     * TODO: 解析上传的Excel文件
     * @Param
     * @Param
     * @Return
     */
    private List<UploadFileData> parseExcelFile(MultipartFile file) throws IOException {
        List<UploadFileData> list = new ArrayList<UploadFileData>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个sheet

            //获取表头验证
            Row headRow = sheet.getRow(0);
            if (headRow == null) {
                throw new IllegalArgumentException("表头不能为空");
            }

            //构建表头映射
            Map<String, Integer> headerMap = buildHeaderMap(headRow);

            //从第二行开始读取数据（跳过表头）
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                UploadFileData data = parseExcelRow(row, headerMap);
                if (data != null) {
                    list.add(data);
                }
            }
        }
        return list;
    }

    /**
     * TODO: 构建表头映射（Excel）
     * @Param
     * @Param
     * @Return
     */
    private Map<String, Integer> buildHeaderMap(Row headRow) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headRow.getLastCellNum(); i++) {
            Cell cell = headRow.getCell(i);
            if (cell != null) {
                String headerName = getCellValue(cell).trim();
                headerMap.put(headerName, i);
            }
        }
        //验证必要字段
        validateHeaders(headerMap);
        return headerMap;
    }

    /**
     * TODO: 获取Excel单元格值
     * @Param
     * @Param
     * @Return
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 避免科学计数法和 .0 结尾
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * TODO: 验证表头是否包含必要字段
     * @Param
     * @Param
     * @Return
     */
    private void validateHeaders(Map<String, Integer> headerMap) {
        List<String> missingHeaders = new ArrayList<>();

        for (String headerName : EXPECTED_HEADERS) {
            if (!headerMap.containsKey(headerName)) {
                missingHeaders.add(headerName);
            }
        }

        if (!missingHeaders.isEmpty()) {
            throw new IllegalArgumentException("表头缺少必要字段：" + String.join(",", missingHeaders));
        }
    }

    /**
     * TODO: 解析Excel行数据
     * @Param
     * @Param
     * @Return UploadFileData uploadFileData 单行数据
     */
    private UploadFileData parseExcelRow(Row row, Map<String, Integer> headerMap) {
        try{
            UploadFileData data = new UploadFileData();

            data.setClass_code(getCellValue(row.getCell(headerMap.get("级别代码"))));
            data.setClass_name(getCellValue(row.getCell(headerMap.get("级别名称"))));
            data.setSub_code(getCellValue(row.getCell(headerMap.get("子类代码"))));
            data.setSub_name(getCellValue(row.getCell(headerMap.get("子类名称"))));
            data.setRemark(getCellValue(row.getCell(headerMap.get("备注"))));

            String personnel_code = getCellValue(row.getCell(headerMap.get("所属业务人员")));
            data.setPersonnel_code(personnel_code.equals("")?null:personnel_code);

            if(data.isEmpty()){
                return null;
            }
            return data;
        } catch (Exception e) {
            // 记录解析错误但继续处理其他行
            System.err.println("解析第 " + (row.getRowNum() + 1) + " 行时出错: " + e.getMessage());
            return null;
        }
    }

}
