package org.example.processserver.Utils;

import lombok.extern.slf4j.Slf4j;
import org.example.processserver.pojo.*;

import org.example.processserver.mapper.db1.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
public class Process_Incre {

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
    private t_entrustMapper_M teMapper;

    @Autowired
    private t_doneMapper_M tdoneMapper;

    @Autowired
    private t_hold_detailMapper_M thdMapper;


    //定时指定增量数据处理任务

    public int main(String tableName) throws Exception {
        log.info("【增量数据加密处理】 ====================== 增量数据加密处理开始： ======================");
        long stime = 0L;
        long etime = 0L;
        double durationInMinutes = 0.0;
        if (tableName.equals("t_client_sett")) {
            log.info("【增量数据加密处理】 ====================== 资金对账表（T_CLIENT_SETT）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTCS();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 资金对账表（T_CLIENT_SETT）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 资金对账表（T_CLIENT_SETT）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_close_detail")) {
            log.info("【增量数据加密处理】 ====================== 平仓明细表（T_CLOSE_DETAIL）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTCD();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 平仓明细表（T_CLOSE_DETAIL）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 平仓明细表（T_CLOSE_DETAIL）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_delivery")) {
            log.info("【增量数据加密处理】 ====================== 交割表（T_DELIVERY）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTD();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 交割表（T_DELIVERY）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 交割表（T_DELIVERY）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_execute_result")) {
            log.info("【增量数据加密处理】 ====================== 行权表（T_EXECUTE_RESULT）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTER();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 行权表（T_EXECUTE_RESULT）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 行权表（T_EXECUTE_RESULT）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_hold_balance")) {
            log.info("【增量数据加密处理】 ====================== 持仓汇总表（T_HOLD_BALANCE）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTHB();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 持仓汇总表（T_HOLD_BALANCE）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }

            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 持仓汇总表（T_HOLD_BALANCE）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_fund_jour")) {
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_FUND_JOUR）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTFJ();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 资金流水表（T_FUND_JOUR）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }
            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_FUND_JOUR）加密处理结束，总执行时长：{} 分钟 ======================", total_time);
        } else if (tableName.equals("t_entrust")) {
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_ENTRUST）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTE();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 资金流水表（T_ENTRUST）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }

            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_ENTRUST）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_done")) {
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_DONE）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTDONE();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 资金流水表（T_DONE）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }

            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_DONE）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else if (tableName.equals("t_hold_detail")) {
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_HOLD_DETAIL）加密处理开始： ======================");
            stime = System.currentTimeMillis();
            try {
                overWriteTHD();
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "【增量数据加密处理】 资金流水表（T_HOLD_DETAIL）加密处理异常：" + e.getMessage();
                log.error(errorMsg);
                return 1;
            }

            etime = System.currentTimeMillis();
            durationInMinutes = (etime - stime) / 60000.0; // 转换为分钟
            String total_time = String.format("%.2f", durationInMinutes);
            log.info("【增量数据加密处理】 ====================== 资金流水表（T_HOLD_DETAIL）加密处理结束，总执行时长：{} 分钟 ======================", total_time);

        } else {
            log.error("【增量数据加密处理】 未知的表名：{}", tableName);
            return 1;
        }
        return 0;
    }



    public void overWriteTCS() throws Exception {
        //输出增量数据量
        int incrementCount = tcsMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量：{} ======================", incrementCount);

        //删除临时表
        tcsMapper.deleteFrom_TCS_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = tcsMapper.TCS_B_getDistinct();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");

        for (int i = 0; i < encryptFields.size(); i++) {
            //加密字段：cliend_id,fund_account_id
            //原始字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            //加密字段
            String encrypt_fundaccount_id = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            String encrypt_client_id = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());

            tcsMapper.insertInto_TCS_encrypt_temp(client_id, encrypt_client_id, fund_account_id, encrypt_fundaccount_id);
        }
        if (!encryptFields.isEmpty()) {
            tcsMapper.overWrite_increment();
        }

    }

    public void overWriteTD() throws Exception {
        int incrementCount = tdMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量：{} ======================", incrementCount);

        //删除临时表
        tdMapper.deleteFrom_TD_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = tdMapper.TD_getDistinct();


        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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

        if (!encryptFields.isEmpty()) {
            tdMapper.overWrite_increment();
        }

    }

    public void overWriteTER() throws Exception {
        int incrementCount = terMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量：{}======================", incrementCount);

        //删除临时表数据
        terMapper.deleteFrom_TER_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = terMapper.TER_getDistinct();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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

        if (!encryptFields.isEmpty()) {
            terMapper.overWrite_increment();
        }

    }

    public void overWriteTHB() throws Exception {
        int incrementCount = thbMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量：{} ======================", incrementCount);

        thbMapper.deleteFrom_THB_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = thbMapper.THB_getDistinct();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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
        if (!encryptFields.isEmpty()) {
            thbMapper.overWrite_increment();
        }

    }


    public void overWriteTCD() throws Exception {
        int incrementCount = tcdMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量： {}======================", incrementCount);

        tcdMapper.delete_TCD_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = tcdMapper.TCD_getDistinct();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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
        if (!encryptFields.isEmpty()) {
            tcdMapper.overWrite_increment();
        }

    }

    public void overWriteTFJ() throws Exception {
        int incrementCount = tfjMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量： {}======================", incrementCount);

        tfjMapper.delete_TFJ_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = tfjMapper.TFJ_encrypt_field();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
        for (int i = 0; i < encryptFields.size(); i++) {
            //资金流水表CLIENT_ID加密、FUND_ACCOUNT_ID加密
            //原字段
            String client_id = encryptFields.get(i).getClient_id();
            String fund_account_id = encryptFields.get(i).getFund_account_id();
            //加密字段
            String encrypt_text1 = SM4Util.cipherTextCBC(encryptFields.get(i).getClient_id());
            String encrypt_text2 = SM4Util.cipherTextCBC(encryptFields.get(i).getFund_account_id());
            tfjMapper.insertInto_TFJ_encrypt_temp(client_id, encrypt_text1, fund_account_id, encrypt_text2);
        }
        if (!encryptFields.isEmpty()) {
            tfjMapper.overWrite_increment();
        }
    }

    public void overWriteTE() throws Exception {
        int incrementCount = teMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量： {}======================", incrementCount);

        teMapper.deleteFrom_TE_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = teMapper.TE_encrypt_field();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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
            teMapper.insertInto_TE_encrypt_temp(client_id, encrypt_text1, fund_account_id, encrypt_text2, trade_account_id, encrypt_text3);
        }
        if (!encryptFields.isEmpty()) {
            teMapper.overWrite_increment();
        }

    }

    private void overWriteTDONE() throws Exception {
        int incrementCount = tdoneMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量： {}======================", incrementCount);

        tdoneMapper.deleteFrom_TDONE_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = tdoneMapper.TDONE_encrypt_field();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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
        if (!encryptFields.isEmpty()) {
            tdoneMapper.overWrite_increment();
        }
    }



    private void overWriteTHD() throws Exception {
        int incrementCount = thdMapper.Increment_getCount();
        log.info(" ====================== 增量总数据量： {}======================", incrementCount);

        thdMapper.deleteFrom_THD_encrypt_temp();

        //获取待加密字段 distinct
        List<encrypt_field> encryptFields = thdMapper.THD_encrypt_field();

        //加密处理,回写临时表
        log.info(" ====================== 增量数据加密处理中 ======================");
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

            thdMapper.insertInto_THD_encrypt_temp(fund_account_id, encrypt_fundaccount_id, trade_account_id, encrypt_tradeaccount_id, client_id, encrypt_client_id);
        }
        if (!encryptFields.isEmpty()) {
            thdMapper.overWrite_increment();
        }
    }
}
