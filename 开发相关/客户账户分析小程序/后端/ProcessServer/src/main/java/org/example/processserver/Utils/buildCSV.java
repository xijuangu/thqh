package org.example.processserver.Utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.processserver.mapper.db1.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PY.Lu
 * @date 2024/10/29
 * @Description
 */
@Service
@Slf4j
public class buildCSV {
    @Autowired
    private buildCSV_init build;
    @Autowired
    private LocationMapper locationMapper;

    //生成CSV文件
    public void generateCSV(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,@RequestParam("tableName") String tableName,@RequestParam("type")int type) throws Exception {
        //需要改写持久层使用的sql语句，该写为基于开始日期和结束日期的查询，一般情况下开始和结束日期都为当天日期，只有在特殊情况下才会使用其他开始和结束日期去指定查询范围
        log.info(" ====================== 生成{}表CSV文件 ======================", tableName);
        if(tableName.equals("S_KINGSTAR_CLIENT_SETT")){
            TCS_CSV(startDate, endDate,tableName,type);
        }else if(tableName.equals("S_KINGSTAR_CLOSE_DETAIL")){
            TCD_CSV(startDate, endDate,tableName,type);
        }else if(tableName.equals("S_KINGSTAR_HOLD_BALANCE")){
            THB_CSV(startDate, endDate,tableName,type);
        }else if(tableName.equals("S_KINGSTAR_DELIVERY")){
            TD_CSV(startDate, endDate,tableName,type);
        }else if(tableName.equals("S_KINGSTAR_EXECUTE_RESULT")) {
            TER_CSV(startDate, endDate,tableName,type);
        }else if(tableName.equals("S_KINGSTAR_PRODUCT")){
            TP_CSV(startDate, endDate,tableName,type);
        }else if(tableName.equals("S_KINGSTAR_PUB_DATE")){
            TPD_CSV(startDate, endDate,tableName,type);
        }

        log.info(" --------------------------------------------------------------------------------------------------------- ");

    }

    //生成check.json文件---暂时不使用，生成check文件环节通过linux脚本进行
    public void generateCheckJson(@RequestParam("type") Integer type) throws Exception {
        //获取CSV文件的行数、大小、MD5值数据
        String mianPath = "";
        if(type==1){
            mianPath = locationMapper.getLocation().getInit_all_Loca()+"/";
        }else if(type==2){
            mianPath = locationMapper.getLocation().getIncre_all_Loca()+"/";
        }else if(type==3){
            mianPath = locationMapper.getLocation().getIncre_custom_Loca()+"/";
        }


        //获取当前日期
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // 将日期减去一天，得到昨天的日期
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        String dateYesterday = formatter.format(yesterday);
        String mainPath_today = mianPath + dateYesterday;

        SimpleDateFormat formatter1= new SimpleDateFormat("HHmmss");
        Date date = new Date(System.currentTimeMillis());
        String createTime = formatter1.format(date);
//        System.out.println(createTime);

        //在加密处理结束之后获取每个CSV文件的大小、行数和MD5值
//        String jsonName = "check_"+dateYesterday+"_"+createTime+".json";
        String jsonName = "check_"+dateYesterday+".json";
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(mainPath_today+"/"+jsonName), "UTF-8");
        //定义好接收数据的数据结构
        JSONObject jsonObject = new JSONObject();

        //遍历目录下所有CSV文件
        File file = new File(mainPath_today);
        File[] files = file.listFiles();

        for (File f : files) {
            if (f.isFile()) {
                String fileName = f.getName();
                String filePath = mainPath_today + "/" + fileName;

                String md5 = MD5_generateUtil.generateMD5(filePath);
                long size = f.length();
                int rowCount = CSVFileData.rowCount(filePath);

                Map<String, String> inmap = new HashMap<>();
                inmap.put("rowCount", String.valueOf(rowCount));
                inmap.put("size", String.valueOf(size));
                inmap.put("md5", md5);
                jsonObject.put(fileName, inmap);

            }
        }

        osw.write(jsonObject.toJSONString());
        osw.flush();
        osw.flush();
        osw.close();

        System.out.println("生成check.json文件已生成");
        System.out.println("生成check.json文件路径："+mainPath_today);
    }

    //创建存储数据的目录
    public static void buildDictory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String buildFile(String startDate, String endDate, String tableName,int type){

        //获取当前日期
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // 将日期减去一天，得到昨天的日期
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        String dateYesterday = formatter.format(yesterday);

        //在生成CSV文件之前先创建存储数据的目录,一个日期一个目录
        String Str1 = new String();

        if(type==1){
            Str1 = locationMapper.getLocation().getInit_all_Loca()+"/";
        }else if(type==2&&startDate.equals(endDate)){
            Str1 = locationMapper.getLocation().getIncre_all_Loca()+"/";
        }else if(type==2&&!startDate.equals(endDate)){
            Str1 = locationMapper.getLocation().getIncre_custom_Loca()+"/";
        }

        buildDictory(Str1);

        buildDictory(Str1+"/"+dateYesterday);
        //定义CSV文件名
        String Str2 = "/"+tableName+"_"+dateYesterday+".data";
        if(!startDate.equals(endDate)){
            Str2 = "/"+tableName+"_"+startDate+"_"+endDate+".data";
        }

        String fileName = Str1+dateYesterday+Str2;

        File file = new File(fileName);

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return fileName;
    }



    //生成TCS CSV文件
    public  void TCS_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);

        build.t_client_sett(type,fileName,startDate,endDate);
    }
    //生成TCD CSV文件
    public  void TCD_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);
        build.t_close_detail(type,fileName,startDate,endDate);
    }
    //生成THB CSV文件
    public  void THB_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);

        build.t_hold_balance(type,fileName,startDate,endDate);
    }
    //生成TD CSV文件
    public void TD_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);

        build.t_delivery(type,fileName,startDate,endDate);

    }
    //生成TER CSV文件
    public void TER_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);

        build.t_execute_result(type,fileName,startDate,endDate);
    }

    //生成TP CSV文件
    public void TP_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);

        build.t_product(type,fileName,startDate,endDate);

    }

    //生成TPD CSV文件
    public void TPD_CSV(String startDate, String endDate, String tableName,int type) throws Exception {
        String fileName = buildFile(startDate,endDate,tableName,type);

        build.t_pub_date(type,fileName,startDate,endDate);
    }

}
