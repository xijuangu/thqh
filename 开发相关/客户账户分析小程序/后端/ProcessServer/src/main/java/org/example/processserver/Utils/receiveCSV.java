package org.example.processserver.Utils;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;
import org.example.processserver.pojo.*;
import org.example.processserver.mapper.db1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author PY.Lu
 * @date 2024/11/1
 * @Description 读取控股数仓传回的CSV文件内容并持久化到数据库
 */
@Service
@Slf4j
public class receiveCSV {
    //汇总
    @Autowired
    private ads_ptaMapper ads_ptaM;


    @Autowired
    private ads_cfsMapper ads_cfsM;

    @Autowired
    private ads_faaMapper ads_faaM;


    //明细
    @Autowired
    private ads_ptadMapper ads_ptadM;

    @Autowired
    private ads_csfdMapper ads_cfsdM;


    @Autowired
    private LocationMapper location;


    @Autowired
    private CheckAmountUtil checkAmountUtil;


    /**
     *
     * @param csv 接收的CSV文件名称
     * @param alias 接收的CSV文件别名
     * @param assign_date 接收的日期
     * @return 处理结果字符串
     * @throws IOException
     * @throws CsvValidationException
     */
    public String readCSV_summary(String csv, String alias, String assign_date) throws IOException, CsvValidationException {
        //获取上一天日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        String dateYesterday = sdf.format(yesterday);


        //如果assign_date不为空，则使用assign_date
        if (assign_date != null && !assign_date.equals("")) {
            dateYesterday = assign_date;
        }

        //当日期文件不存在时，给到判断提示
        String MainPath = location.getLocation().getReceive_Loca() + "/" + dateYesterday + "/";

        String trueName = getFileTrueName(MainPath, csv, dateYesterday);
        if (trueName.equals("false1")) {
            String error = MainPath +"目录不存在！";
            return error;
        }
        if (trueName.equals("false2")) {
            String error = MainPath + "目录下无"+csv+"相关的data文件！";
            return error;
        }
        String path = MainPath + trueName;

        log.info("【接收Data文件插入备库】 ====================== 所接收Data文件插入备库： ======================");

        log.info(" ====================== 清空备库{}表数据 ======================", csv);
        //提前清空数据库表内的数据
        truncateTable(alias);

        log.info(" ====================== 读取Data文件路径:{} ======================", path);

        CSVFileData getCsvData = new CSVFileData();
        FileInputStream in = new FileInputStream(path);
        CSVReader reader = new CSVReader(new InputStreamReader(in, "UTF-8"));
        List<Object> list = new ArrayList<>();
        String[] str;
        int i = 0, count = 0;
        int start = 0, end = 0;
        int batchSize = 50000; // 每次读5w行
        int batchCounter = (getCsvData.rowCount(path) + batchSize - 1) / batchSize; // 计算批次

        if (getCsvData.rowCount(path) == 0) {
            log.info(path + "Data文件为空！");
            log.info(" ====================== Data文件为空！ ======================");
            return path + "对应的Data文件为空！";
        }
        long startTime = System.currentTimeMillis();

        log.info(" ====================== Data总行数:{} ======================", getCsvData.rowCount(path));

        long endTime = System.currentTimeMillis();
        //转换成分钟
        long time = (endTime - startTime) / 1000;
        log.info("遍历csv总共耗时：" + time + "秒");

        log.info(" ====================== 批次数量:{} ======================", batchCounter);

        while ((str = reader.readNext()) != null) {
            str[0] = str[0] + "#";
            str = str[0].split("\u0001");
            str[str.length - 1] = str[str.length - 1].replace("#", "");

            if (alias.equals("ads_cfs")) {
                ads_cfs ADS_CFS = new ads_cfs();
                list.add(ADS_CFS.setData(str, ADS_CFS));
            } else if (alias.equals("ads_faa")) {
                ads_faa ADS_FAA = new ads_faa();
                list.add(ADS_FAA.setData(str, ADS_FAA));
            } else if (alias.equals("ads_pta")) {
                ads_pta ADS_PTA = new ads_pta();
                list.add(ADS_PTA.setData(str, ADS_PTA));
            } else if (alias.equals("ads_cfsd")) {
                ads_cfsd ADS_CFSD = new ads_cfsd();
                list.add(ADS_CFSD.setData(str, ADS_CFSD));
            } else if (alias.equals("ads_ptad")) {
                ads_ptad ADS_PTAD = new ads_ptad();
                list.add(ADS_PTAD.setData(str, ADS_PTAD));
            } else {
                log.info(" ====================== 请检查该表别名是否存在改动，支持的表别名为[ads_cfs、ads_faa、ads_pta、ads_cfsd、ads_ptad]:{} ======================", alias);
                log.info(" ====================== 处理中止 ======================");
                break;
            }

            i++;

            if (i == batchSize) {

                startTime = System.currentTimeMillis();
                start = count * batchSize + 1;
                end = start + batchSize - 1;

                if (alias.equals("ads_cfs")) {
                    ads_cfsM.insertList_ads_cfs(list);
                } else if (alias.equals("ads_faa")) {
                    ads_faaM.insertList_ads_faa(list);
                } else if (alias.equals("ads_pta")) {
                    ads_ptaM.insertList_ads_pta(list);
                } else if (alias.equals("ads_cfsd")) {
                    ads_cfsdM.insertList_ads_cfsd(list);
                } else if (alias.equals("ads_ptad")) {
                    ads_ptadM.insertList_ads_ptad(list);
                }
                endTime = System.currentTimeMillis();
                //转换成分钟
                time = (endTime - startTime) / 1000;
                log.info("读取第{}批次数据，从第{}到{}行，耗时：{}秒", count + 1, start, end, time);
                list.clear();
                i = 0;
                count++;
            }

        }
        if (list != null && list.size() > 0) {
            log.info("读取第{}批次数据，从第{}到{}行", count, end + 1, getCsvData.rowCount(path));

            if (alias.equals("ads_cfs")) {
                ads_cfsM.insertList_ads_cfs(list);
            } else if (alias.equals("ads_faa")) {
                ads_faaM.insertList_ads_faa(list);
            } else if (alias.equals("ads_pta")) {
                ads_ptaM.insertList_ads_pta(list);
            } else if (alias.equals("ads_cfsd")) {
                ads_cfsdM.insertList_ads_cfsd(list);
            } else if (alias.equals("ads_ptad")) {
                ads_ptadM.insertList_ads_ptad(list);
            }
            list.clear();
        }
        log.info("【接收CSV文件写入备库】 ====================== 数据导入完成 ======================");
        reader.close();

        checkAmountUtil.CheckAmount_DW(MainPath, dateYesterday);

        return csv+"表数据导入完成！";

    }


    public static <T> List<T> getCsvData(File file, Class<T> clazz) {
        InputStreamReader in = null;// 读取输入流
        FileInputStream fis = null;
        CsvToBean<T> csvToBean = null; // 转换器,将 CSV 转换为 Java 对象
        try {
            fis = new FileInputStream(file);
            in = new InputStreamReader(fis, "GB2312");


            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();// 策略，将 CSV 第一行的标题映射到 Java 对象属性
            strategy.setType(clazz);//将传入的目标类型 clazz 设置为映射策略的类型，CSV列会被正确映射到目标类型
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvToBean.parse();

    }

    /**
     *
     * @param MainPath 服务器上的CSV文件接收目录
     * @param fileName 接收的CSV文件的名称
     * @param assign_date 接收的日期
     * @return 接收的CSV文件的绝对路径
     */
    public String getFileTrueName(String MainPath, String fileName, String assign_date) {
        //获取当前日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // 将日期减去一天，得到昨天的日期
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        String dateYesterday = formatter.format(yesterday);

        if (assign_date != null && !assign_date.equals("")) {
            dateYesterday = assign_date;
        }


        File file = new File(MainPath);
        if (!file.exists()) {
            log.info("{}目录不存在！", MainPath);
            return "false1";
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".data")) {
                String[] fileNameSplit = f.getName().split("_" + dateYesterday);

                if (fileNameSplit[0].equals(fileName)) {
                    String[] str1 = f.getName().split("_");
                    String[] str2 = str1[str1.length - 1].split("\\.");
                    if (str2[0].equals(dateYesterday)) {
                        return f.getName();
                    }
                }
            }
        }
        return "false2";
    }

    /**
     *
     * @param alias 接收CSV文件的别名
     * @Description 在按批次读取CSV文件并将其中内容写入数据库之前需要先清空要写入的表
     */
    public void truncateTable(String alias) {
        if (alias.equals("ads_cfs")) {
            ads_cfsM.truncate_ads_cfs();
        } else if (alias.equals("ads_faa")) {
            ads_faaM.truncate_ads_faa();
        } else if (alias.equals("ads_pta")) {
            ads_ptaM.truncate_ads_pta();
        } else if (alias.equals("ads_cfsd")) {
            ads_cfsdM.truncate_ads_cfsd();
        } else if (alias.equals("ads_ptad")) {
            ads_ptadM.truncate_ads_ptad();
        }
    }

}

