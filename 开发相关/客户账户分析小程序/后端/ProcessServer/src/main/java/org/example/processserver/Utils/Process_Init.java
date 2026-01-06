package org.example.processserver.Utils;

import lombok.extern.slf4j.Slf4j;
import org.example.processserver.pojo.*;
import org.example.processserver.mapper.db1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Process_Init {
    @Autowired
    private t_client_settMapper_M tcsMapper;

    @Autowired
    private t_close_detailMapper_M tcdMapper;

    @Autowired
    private t_deliveryMapper_M tdMapper;

    @Autowired
    private t_execute_resultMapper_M terMapper;

    @Autowired
    private t_hold_balanceMapper_M thbMapper;


    @Autowired
    private t_fund_jourMapper_M tfjMapper;

    @Autowired
    private t_doneMapper_M tdoneMapper;

    @Autowired
    private t_entrustMapper_M tentrustMapper;

    @Autowired
    private t_hold_detailMapper_M thdMapper;

    public int process_init(String tableName) throws Exception {
        //指定表进行加密处理
        log.info("【初始化全量数据加密处理】 ====================== 初始化全量数据加密处理开始： ======================");

        long stime = 0L;
        long etime = 0L;
        double durationInMinutes = 0.0;

        if (tableName.equals("t_client_sett")) {
            log.info("【初始化全量数据加密处理】 ====================== 资金对账表（T_CLIENT_SETT）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTCS();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0;
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 资金对账表（T_CLIENT_SETT）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_close_detail")) {
            log.info("【初始化全量数据加密处理】 ====================== 平仓明细表（T_CLOSE_DETAIL）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTCD();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 平仓明细表（T_CLOSE_DETAIL）加密处理异常：{}", e.getMessage());
                return 1;

            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 平仓明细表（T_CLOSE_DETAIL）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_delivery")) {

            log.info("【初始化全量数据加密处理】 ====================== 交割表（T_DELIVERY）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTD();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 交割表（T_DELIVERY）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0;
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 交割表（T_DELIVERY）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_execute_result")) {
            log.info("【初始化全量数据加密处理】 ====================== 行权表（T_EXECUTE_RESULT）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTER();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 行权表（T_EXECUTE_RESULT）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 行权表（T_EXECUTE_RESULT）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_hold_balance")) {
            log.info("【初始化全量数据加密处理】 ====================== 持仓汇总表（T_HOLD_BALANCE）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTHB();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 持仓汇总表（T_HOLD_BALANCE）加密处理异常：{}", e.getMessage());

                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 持仓汇总表（T_HOLD_BALANCE）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_fund_jour")) {
            log.info("【初始化全量数据加密处理】 ====================== 资金流水表（T_FUND_JOUR）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTFJ();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 资金流水表（T_FUND_JOUR）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 资金流水表（T_FUND_JOUR）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_entrust")) {
            log.info("【初始化全量数据加密处理】 ====================== 委托流水表（T_ENTRUST）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTE();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 委托流水表（T_ENTRUST）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 委托流水表（T_ENTRUST）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_done")) {
            log.info("【初始化全量数据加密处理】 ====================== 成交表（T_DONE）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTDONE();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 成交表（T_DONE）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 成交表（T_DONE）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_hold_detail")) {
            log.info("【初始化全量数据加密处理】 ====================== 持仓明细表（T_HOLD_DETAIL）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTHD();
            } catch (Exception e) {

                e.printStackTrace();
                String error_msg = "【初始化全量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error("【初始化全量数据加密处理】 持仓明细表（T_HOLD_DETAIL）加密处理异常：{}", e.getMessage());
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【初始化全量数据加密处理】 ====================== 持仓明细表（T_HOLD_DETAIL）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else {
            log.error("【初始化全量数据加密处理】 未知的表名：{}", tableName);
            return 1;
        }
        return 0;
    }


    public void overWriteTCS() throws Exception {
        tcsMapper.deleteFrom_TCS_encrypt_temp();

        List<encrypt_field> encryptFields = tcsMapper.TCS_B_getDistinct();//for trade and fund

        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：cliend_id,fund_account_id
            //原始字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            //加密字段
//            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
//            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            //            tcsMapper.insertInto_TCS_encrypt_temp(client_id, encrypt_client_id, fund_account_id, encrypt_fundaccount_id);

            //解密字段
            String decrypt_fundaccount_id = SM4Util.decryptTextCBC(encryptFields.get(i).getFund_account_id());
            String decrypt_client_id = SM4Util.decryptTextCBC(encryptFields.get(i).getFund_account_id());

            tcsMapper.insertInto_TCS_encrypt_temp(client_id, decrypt_client_id, fund_account_id, decrypt_fundaccount_id);
        }

        int count = tcsMapper.getCount();

        log.info(" ====================== 总数据量： ======================", count);

        int start = 1, batch_size = 10000;
        for (int i = start; i < count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            System.out.println();

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                tcsMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);

        }

    }

    public void overWriteTD() throws Exception {
        tdMapper.deleteFrom_TD_encrypt_temp();
        List<encrypt_field> encryptFields = tdMapper.TD_getDistinct();

        log.info(" ====================== 分批处理开始 ======================");

        tdMapper.deleteFrom_TD_encrypt_temp();
        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：client_id,fund_account_id,trade_account_id
            //原始字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();
            //加密字段
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_tradeaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id() == null ? "" : encryptFields.get(i).getTrade_account_id());

            tdMapper.insertInto_TD_encrypt_temp(client_id, encrypt_client_id, fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id);
        }

        int count = tdMapper.getCount();

        log.info(" ====================== 总数据量： ======================", count);

        int start = 1, batch_size = 10000;
        for (int i = start; i < count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                tdMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);

        }

    }

    public void overWriteTER() throws Exception {
        terMapper.deleteFrom_TER_encrypt_temp();
        List<encrypt_field> encryptFields = terMapper.TER_getDistinct();
        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：fund_account_id,trade_account_id,client_id
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();
            //加密字段
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_tradeaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id());
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            terMapper.insertInto_TER_encrypt_temp(fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id, client_id, encrypt_client_id);
        }

        int count = terMapper.getCount(), batch_size = 10000;

        log.info(" ====================== 总数据量： ======================", count);

        int start = 1;
        for (int i = start; i <= count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                terMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);
        }

        System.out.println("加密处理完成...");

    }

    public void overWriteTFJ() throws Exception {
        tfjMapper.delete_TFJ_encrypt_temp();
        List<encrypt_field> encryptFields = tfjMapper.TFJ_encrypt_field();

        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：fund_account_id,client_id
            //原始字段
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String client_id = encryptFields.get(i).getClient_id();
            //加密字段
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());

            tfjMapper.insertInto_TFJ_encrypt_temp(client_id, encrypt_client_id, fund_account_id, encrypt_fundaccount_id);
        }

        int count = tfjMapper.getCount();

        log.info(" ====================== 总数据量： ======================", count);

        int start = 1, batch_size = 10000;
        for (int i = start; i < count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                tfjMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);

        }

    }

    public void overWriteTHB() throws Exception {
        thbMapper.deleteFrom_THB_encrypt_temp();
        List<encrypt_field> encryptFields = thbMapper.THB_getDistinct();

        log.info(" ====================== 分批处理开始 ======================");
        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：fund_account_id,trade_account_id,client_id
            //原始字段
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();
            String client_id = encryptFields.get(i).getClient_id();
            //加密字段
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_tradeaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id());
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());

            thbMapper.insertInto_THB_encrypt_temp(fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id, client_id, encrypt_client_id);
        }

        int count = thbMapper.getCount(), batch_size = 10000;
        log.info(" ====================== 总数据量： ======================", count);
        int start = 1;
        for (int i = start; i <= count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            System.out.println();
            System.out.print(n + "-" + m + "批次");

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                thbMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);
        }

    }

    public void overWriteTCD() throws Exception {
        tcdMapper.delete_TCD_encrypt_temp();
        List<encrypt_field> encryptFields = tcdMapper.TCD_getDistinct();

        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：cliend_id,fund_account_id，trade_account_id
            //原始字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();
            //加密字段
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_tradeaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id());
            tcdMapper.insertInto_TCD_encrypt_temp(client_id, encrypt_client_id, fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id);
        }

        int count = tcdMapper.getCount();
        log.info(" ====================== 总数据量： ======================", count);

        int start = 1;
        int batch_size = 10000;
        for (int i = start; i <= count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                tcdMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);
        }

    }

    public void overWriteTDONE() throws Exception {
        tdoneMapper.deleteFrom_TDONE_encrypt_temp();
        List<encrypt_field> encryptFields = tdoneMapper.TDONE_encrypt_field();

        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：cliend_id,fund_account_id，trade_account_id
            //原始字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();

            //加密字段
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_tradeaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id());

            tdoneMapper.insertInto_TDONE_encrypt_temp(client_id, encrypt_client_id, fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id);
        }


        int count = tdoneMapper.getCount();
        log.info(" ====================== 总数据量： ======================", count);

        int Start = 1;
        int batch_size = 10000;

        for (int i = Start; i <= count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                tdoneMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);
        }

    }

    public void overWriteTE() throws Exception {
        tentrustMapper.deleteFrom_TE_encrypt_temp();
        List<encrypt_field> encryptFields = tentrustMapper.TE_encrypt_field();

        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //委托流水表CLIENT_ID加密、FUND_ACCOUNT_ID加密、TRADE_ACCOUNT_ID加密
            //原字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();
            //加密字段
            String encrypt_text1 = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            String encrypt_text2 = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_text3 = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id());

            tentrustMapper.insertInto_TE_encrypt_temp(client_id, encrypt_text1, fund_account_id, encrypt_text2, trade_account_id, encrypt_text3);
        }

        int count = tentrustMapper.getCount();

        log.info(" ====================== 总数据量： ======================", count);

        int Start = 1;
        int batch_size = 10000;
        for (int i = Start; i <= count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                tentrustMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);


        }

    }

    public void overWriteTHD() throws Exception {
        thdMapper.deleteFrom_THD_encrypt_temp();
        List<encrypt_field> encryptFields = thdMapper.THD_encrypt_field();

        log.info(" ====================== 分批处理开始 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：fund_account_id,trade_account_id,client_id
            //原始字段
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            String trade_account_id = encryptFields.get(i).getTrade_account_id();
            String client_id = encryptFields.get(i).getClient_id();
            //加密字段
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_tradeaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getTrade_account_id());
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());

            thdMapper.insertInto_THD_encrypt_temp(fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id, client_id, encrypt_client_id);
        }

        int count = thdMapper.getCount();
        log.info(" ====================== 总数据量： ======================", count);

        int Start = 1;
        int batch_size = 10000;
        for (int i = Start; i <= count; i += batch_size) {
            int n = i, m = i + batch_size - 1;

            Long stime = System.currentTimeMillis();

            if (!encryptFields.isEmpty()) {
                thdMapper.overWrite(n, m);
            }

            Long etime = System.currentTimeMillis();
            double durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("{} - {}批次加密处理执行时长： {} 分钟.", n, m, total_time);
        }

    }


}
