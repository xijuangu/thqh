package org.example.processserver.Utils;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.example.processserver.pojo.*;
import org.example.processserver.mapper.db1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class buildCSV_init {

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
    private t_productMapper_M tpMapper;

    @Autowired
    private t_pub_dateMapper_M tpdMapper;

    // ================== T_PRODUCT 表导出逻辑 (小数据量表) ==================
    /**
     * 定义 T_PRODUCT 表的CSV文件表头。
     *
     * @return 包含所有列名的字符串数组。
     */
    public String[] t_product_header() {
        String[] header = {
                "product_id",
                "trade_type",
                "market_id",
                "product_name",
                "product_type",
                "hand_amount",
                "delivery_type",
                "ds_date",
                "data_source",
                "busi_date",
                "max_hold",
                "min_hand",
                "max_hand",
                "price_unit",
                "high_limit",
                "low_limit",
                "trade_rule",
                "deliv_date",
                "status",
                "eo_transfee_amt",
                "eo_transfee_rate",
                "ot_transfee_amt",
                "ot_transfee_rate",
                "deliv_transfee_amt",
                "deliv_transfee_rate",
                "spec_margin_amt",
                "spec_margin_rate",
                "hedge_margin_amt",
                "hedge_margin_rate",
                "money_type",
                "oper_code",
                "oper_date",
                "oper_time",
                "denominator",
                "exponent",
                "deliv_rule",
                "hedge_eo_transfee_amt",
                "hedge_eo_transfee_rate",
                "hedge_ot_transfee_amt",
                "hedge_ot_transfee_rate",
                "hedge_deliv_transfee_amt",
                "hedge_deliv_transfee_rate",
                "arbit_eo_transfee_amt",
                "arbit_eo_transfee_rate",
                "arbit_ot_transfee_amt",
                "arbit_ot_transfee_rate",
                "arbit_deliv_transfee_amt",
                "arbit_deliv_transfee_rate",
                "arbit_margin_amt",
                "arbit_margin_rate",
                "foreign_currency_pledge",
                "product_month",
                "deliv_type",
                "strike_type",
                "execute_transfee_amt",
                "execute_transfee_rate",
                "strike_transfee_amt",
                "strike_transfee_rate",
                "hedge_execute_transfee_amt",
                "hedge_execute_transfee_rate",
                "hedge_strike_transfee_amt",
                "hedge_strike_transfee_rate",
                "arbit_execute_transfee_amt",
                "arbit_execute_transfee_rate",
                "arbit_strike_transfee_amt",
                "arbit_strike_transfee_rate",
                "margin_alg_type",
                "quoted_price_unit",
                "transfee_algo",
        };

        return header;
    }

    /**
     * T_PRODUCT 表的数据导出主方法。
     * 该方法目前仅实现了全量导出。
     *
     * @param type      导出类型 (1: 全量, 2: 增量)。
     * @param FileName  CSV文件的完整路径。
     * @param startDate 增量导出的开始日期。
     * @param endDate   增量导出的结束日期。
     * @throws Exception
     */
    public void t_product(int type,String FileName,String startDate,String endDate) throws Exception {
        String[] header = t_product_header();

        // 查询全量数据
        List<t_product> t_products = tpMapper.selectAll();
        if(t_products.size()!=0){
            // 将查询到的数据写入CSV文件
            t_product_writeData(FileName,t_products);
        }
    }

    /**
     * 将 T_PRODUCT 数据列表写入CSV文件。
     *
     * @param FilePath CSV文件的完整路径。
     * @param list     包含t_product对象的列表。
     * @throws Exception
     */
    public void t_product_writeData(String FilePath,List<t_product> list) throws Exception {
        String filPath = FilePath;
        int count = 0;
        // 使用 try-with-resources 语句，确保CSVWriter在使用后自动关闭，无需手动调用close()
        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {
            // 遍历所有t_execute_result对象，并将它们的值写入CSV文件
            for (t_product item : list) {
                if(item==null){
                    break;
                }

                // 将对象的每个字段转换为字符串，存入数组
                String[] row = {
                        String.valueOf(item.getProduct_id()),
                        String.valueOf(item.getTrade_type()),
                        String.valueOf(item.getMarket_id()),
                        String.valueOf(item.getProduct_name()),
                        String.valueOf(item.getProduct_type()),
                        String.valueOf(item.getHand_amount()),
                        String.valueOf(item.getDelivery_type()),
                        String.valueOf(item.getDs_date()),
                        String.valueOf(item.getData_source()),
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getMax_hold()),
                        String.valueOf(item.getMin_hand()),
                        String.valueOf(item.getMax_hand()),
                        String.valueOf(item.getPrice_unit()),
                        String.valueOf(item.getHigh_limit()),
                        String.valueOf(item.getLow_limit()),
                        String.valueOf(item.getTrade_rule()),
                        String.valueOf(item.getDeliv_date()),
                        String.valueOf(item.getStatus()),
                        String.valueOf(item.getEo_transfee_amt()),
                        String.valueOf(item.getEo_transfee_rate()),
                        String.valueOf(item.getOt_transfee_amt()),
                        String.valueOf(item.getOt_transfee_rate()),
                        String.valueOf(item.getDeliv_transfee_amt()),
                        String.valueOf(item.getDeliv_transfee_rate()),
                        String.valueOf(item.getSpec_margin_amt()),
                        String.valueOf(item.getSpec_margin_rate()),
                        String.valueOf(item.getHedge_margin_amt()),
                        String.valueOf(item.getHedge_margin_rate()),
                        String.valueOf(item.getMoney_type()),
                        String.valueOf(item.getOper_code()),
                        String.valueOf(item.getOper_date()),
                        String.valueOf(item.getOper_time()),
                        String.valueOf(item.getDenominator()),
                        String.valueOf(item.getExponent()),
                        String.valueOf(item.getDeliv_rule()),
                        String.valueOf(item.getHedge_eo_transfee_amt()),
                        String.valueOf(item.getHedge_eo_transfee_rate()),
                        String.valueOf(item.getHedge_ot_transfee_amt()),
                        String.valueOf(item.getHedge_ot_transfee_rate()),
                        String.valueOf(item.getHedge_deliv_transfee_amt()),
                        String.valueOf(item.getHedge_deliv_transfee_rate()),
                        String.valueOf(item.getArbit_eo_transfee_amt()),
                        String.valueOf(item.getArbit_eo_transfee_rate()),
                        String.valueOf(item.getArbit_ot_transfee_amt()),
                        String.valueOf(item.getArbit_ot_transfee_rate()),
                        String.valueOf(item.getArbit_deliv_transfee_amt()),
                        String.valueOf(item.getArbit_deliv_transfee_rate()),
                        String.valueOf(item.getArbit_margin_amt()),
                        String.valueOf(item.getArbit_margin_rate()),
                        String.valueOf(item.getForeign_currency_pledge()),
                        String.valueOf(item.getProduct_month()),
                        String.valueOf(item.getDeliv_type()),
                        String.valueOf(item.getStrike_type()),
                        String.valueOf(item.getExecute_transfee_amt()),
                        String.valueOf(item.getExecute_transfee_rate()),
                        String.valueOf(item.getStrike_transfee_amt()),
                        String.valueOf(item.getStrike_transfee_rate()),
                        String.valueOf(item.getHedge_execute_transfee_amt()),
                        String.valueOf(item.getHedge_execute_transfee_rate()),
                        String.valueOf(item.getHedge_strike_transfee_amt()),
                        String.valueOf(item.getHedge_strike_transfee_rate()),
                        String.valueOf(item.getArbit_execute_transfee_amt()),
                        String.valueOf(item.getArbit_execute_transfee_rate()),
                        String.valueOf(item.getArbit_strike_transfee_amt()),
                        String.valueOf(item.getArbit_strike_transfee_rate()),
                        String.valueOf(item.getMargin_alg_type()),
                        String.valueOf(item.getQuoted_price_unit()),
                        String.valueOf(item.getTransfee_algo()),
                };

                // 处理数组中的null值
                String[] getRidNull = replaceNullWithEmpty(row);
                writer.writeNext(getRidNull);
                count++;
                }
            }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("写入数据行数：" + count);
        log.info("生成路径"+filPath);
    }


    // ================== T_PUB_DATE 表等的小数据量表的导出逻辑基本与T_PRODUCT表的一致，后续省略重复注释 ==================
    public String[] t_pub_date_header() {
        String[] header = {
                "busi_date",
                "market_no",
                "trade_flag"
        };

        return header;
    }
    public void t_pub_date(int type,String FileName,String startDate,String endDate) throws Exception {
        String[] header = t_pub_date_header();
        List<t_pub_date> t_pub_dates = tpdMapper.selectAll();
        if(t_pub_dates.size()!=0)
        {
            t_pub_date_writeData(FileName,t_pub_dates);
        }

    }
    public void t_pub_date_writeData(String FilePath,List<t_pub_date> list) throws Exception {
        String filPath = FilePath;
        int count = 0;
        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {
            // 遍历所有t_execute_result对象，并将它们的值写入CSV文件
            for (t_pub_date item : list) {
                if(item==null){
                    break;
                }
                String[] row = {
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getMarket_no()),
                        String.valueOf(item.getTrade_flag())
                };
                String[] getRidNull = replaceNullWithEmpty(row);
                writer.writeNext(getRidNull);
                count++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("写入数据行数：" + count);
        log.info("生成路径"+FilePath);

    }


    // ================== T_CLIENT_SETT 表导出逻辑 (大数据量表) ==================
    /**
     * 定义 T_CLIENT_SETT 表的CSV文件表头。
     *
     * @return 包含所有列名的字符串数组。
     */
    public String[] t_client_sett_header() {
        String[] header = {
                "busi_date",
                "margin",
                "today_profit",
                "transfee",
                "interest",
                "impawn_money",
                "remain",
                "open_pre_money",
                "credit",
                "call_margin",
                "reserve",
                "lowest_interest",
                "yes_impawn_money",
                "money_type",
                "fund_impawn_in",
                "fund_impawn_out",
                "fund_impawn_margin",
                "opt_premium_income",
                "opt_premium_pay",
                "optstrike_profit",
                "market_deposit",
                "buy_opt_market_value",
                "sell_opt_market_value",
                "brokers_id",
                "client_id",
                "fund_account_id",
                "market_margin",
                "close_profit",
                "hold_profit",
                "float_profit",
                "market_transfee",
                "interest_base",
                "fund_in",
                "fund_out",
                "data_source",
                "ds_date",
                "branch_id",
                "yes_deposit",
                "deposit",
                "yes_rights",
                "rights",
                "strikefee",
                "market_strikefee",
                "performfee",
                "market_performfee",
                "delivery_margin",
                "market_delivery_margin",
                "delivery_transfee",
                "market_delivery_transfee",
                "settfee",
                "market_settfee",
                "holdmovefee",
                "market_holdmovefee",
                "client_name",
                "add_fee1",
                "add_fee2",
                "hold_profit_d",
                "drop_profit_f",
                "other_fee",
                "exch_other_fee",
                "way_money",
                "risk_level",
                "risk_degree1",
                "risk_degree2",
                "deliv_profit",
                "agio_margin",
                "currency_pledge",
                "currency_pledge_available",
                "currency_in",
                "currency_out",
                "spec_margin",
                "spec_exch_margin",
                "spec_deliv_margin",
                "spec_deliv_exch_margin",
                "spec_commi",
                "spec_exch_commi",
                "spec_hold_profit_d",
                "spec_drop_profit_d",
                "spec_drop_profit_f",
                "dyn_end_rights",
                "deliver_frozen_net_fund",
                "frozen_sum_funds",
                "undeliv_profit",
                "hold_profit_f",
                "md_before_fund_in",
                "md_before_fund_out",
                "socrt_openfee",
                "settlementfee",
                "strike_credit",
                "strikeactreceivsum",
                "strikeactpaysum",
                "depositbytrade",
                "fund_impawn_chg",
                "lastdepositbytrade",
                "close_profit_f",
                "lastfundmortgageout",
                "lastfundmortgagein",
                "deposit_bytrade",
                "yes_deposit_bytrade",
                "yes_fund_impawn_out",
                "yes_fund_impawn_in",
                "fund_impawn_market_margin",
                "sett_type",
                "total_transfee",
                "total_market_transfee",
                "execute_transfee",
                "market_execute_transfee",
                "client_no",
                "buyfund",
                "selfund",
                "id",
                "encrypt_lable"
        };
        return header;
    }

    /**
     * T_CLIENT_SETT 表的数据导出主方法。
     * 该方法实现了全量和增量两种导出模式，并对全量导出进行了分批处理。
     *
     * @param type      导出类型 (1: 全量, 2: 增量)。
     * @param FileName  CSV文件的完整路径。
     * @param startDate 增量导出的开始日期。
     * @param endDate   增量导出的结束日期。
     * @throws Exception
     */
    public void t_client_sett(int type,String FileName,String startDate,String endDate) throws Exception {
        String[] header = t_client_sett_header();
        if(type==1){
            // --- 全量导出，分批处理 ---
            // 1. 查询总行数
            int count = tcsMapper.getCount();
            int start=1,batch_size=10000;

            // 2. 循环分批查询和写入
            for(int i=start;i<count;i+=batch_size){
                int n=i,m=i+batch_size-1;
                System.out.print(n+"-"+m+"批次写入"+FileName+"中...");

                // 3. 分批查询数据
                List<t_client_sett> list = tcsMapper.getAll(n,m);

                // 4. 写入当前批次的数据
                t_client_sett_writeData(FileName,list);
            }
        }
        else if(type==2){
            // --- 增量导出 ---
            List<t_client_sett> increment = tcsMapper.getIncrement(startDate,endDate);
            if(increment.size()!=0){
                t_client_sett_writeData(FileName,increment);
            }
        }
    }

    /**
     * 将 T_CLIENT_SETT 数据列表写入CSV文件。
     *
     * @param FilePath CSV文件的完整路径。
     * @param list     包含t_client_sett对象的列表。
     * @throws Exception
     */
    public void t_client_sett_writeData(String FilePath,List<t_client_sett> list) throws Exception {
        String filPath = FilePath;
        int count = 0;

        // 使用追加模式 ("true" 参数) 写入文件
        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {
            // 遍历所有t_execute_result对象，并将它们的值写入CSV文件
            for (t_client_sett item : list) {
                if(item==null){
                    break;
                }
                String[] row = {
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getMargin()),
                        String.valueOf(item.getToday_profit()) ,
                        String.valueOf(item.getTransfee()) ,
                        String.valueOf(item.getInterest()),
                        String.valueOf(item.getImpawn_money()),
                        String.valueOf(item.getRemain()),
                        String.valueOf(item.getOpen_pre_money()),
                        String.valueOf(item.getCredit()),
                        String.valueOf(item.getCall_margin()),
                        String.valueOf(item.getReserve()),
                        String.valueOf(item.getLowest_interest()),
                        String.valueOf(item.getYes_impawn_money()),
                        String.valueOf(item.getMoney_type()),
                        String.valueOf(item.getFund_impawn_in()),
                        String.valueOf(item.getFund_impawn_out()),
                        String.valueOf(item.getFund_impawn_margin()),
                        String.valueOf(item.getOpt_premium_income()),
                        String.valueOf(item.getOpt_premium_pay()),
                        String.valueOf(item.getOptstrike_profit()),
                        String.valueOf(item.getMarket_deposit()),
                        String.valueOf(item.getBuy_opt_market_value()),
                        String.valueOf(item.getSell_opt_market_value()),
                        String.valueOf(item.getBrokers_id()),
                        String.valueOf(item.getClient_id()),
                        String.valueOf(item.getFund_account_id()),
                        String.valueOf(item.getMarket_margin()),
                        String.valueOf(item.getClose_profit()),
                        String.valueOf(item.getHold_profit()),
                        String.valueOf(item.getFloat_profit()),
                        String.valueOf(item.getMarket_transfee()),
                        String.valueOf(item.getInterest_base()),
                        String.valueOf(item.getFund_in()),
                        String.valueOf(item.getFund_out()),
                        String.valueOf(item.getData_source()),
                        String.valueOf(item.getDs_date()),
                        String.valueOf(item.getBranch_id()),
                        String.valueOf(item.getYes_deposit()),
                        String.valueOf(item.getDeposit()),
                        String.valueOf(item.getYes_rights()),
                        String.valueOf(item.getRights()),
                        String.valueOf(item.getStrikefee()),
                        String.valueOf(item.getMarket_strikefee()),
                        String.valueOf(item.getPerformfee()),
                        String.valueOf(item.getMarket_performfee()),
                        String.valueOf(item.getDelivery_margin()),
                        String.valueOf(item.getMarket_delivery_margin()),
                        String.valueOf(item.getDelivery_transfee()),
                        String.valueOf(item.getMarket_delivery_transfee()),
                        String.valueOf(item.getSettfee()),
                        String.valueOf(item.getMarket_settfee()),
                        String.valueOf(item.getHoldmovefee()),
                        String.valueOf(item.getMarket_holdmovefee()),
                        String.valueOf(item.getClient_name()),
                        String.valueOf(item.getAdd_fee1()),
                        String.valueOf(item.getAdd_fee2()),
                        String.valueOf(item.getHold_profit_d()),
                        String.valueOf(item.getDrop_profit_f()),
                        String.valueOf(item.getOther_fee()),
                        String.valueOf(item.getExch_other_fee()),
                        String.valueOf(item.getWay_money()),
                        String.valueOf(item.getRisk_level()),
                        String.valueOf(item.getRisk_degree1()),
                        String.valueOf(item.getRisk_degree2()),
                        String.valueOf(item.getDeliv_profit()),
                        String.valueOf(item.getAgio_margin()),
                        String.valueOf(item.getCurrency_pledge()),
                        String.valueOf(item.getCurrency_pledge_available()),
                        String.valueOf(item.getCurrency_in()),
                        String.valueOf(item.getCurrency_out()),
                        String.valueOf(item.getSpec_margin()),
                        String.valueOf(item.getSpec_exch_margin()),
                        String.valueOf(item.getSpec_deliv_margin()),
                        String.valueOf(item.getSpec_deliv_exch_margin()),
                        String.valueOf(item.getSpec_commi()),
                        String.valueOf(item.getSpec_exch_commi()),
                        String.valueOf(item.getSpec_hold_profit_d()),
                        String.valueOf(item.getSpec_drop_profit_d()),
                        String.valueOf(item.getSpec_drop_profit_f()),
                        String.valueOf(item.getDyn_end_rights()),
                        String.valueOf(item.getDeliver_frozen_net_fund()),
                        String.valueOf(item.getFrozen_sum_funds()),
                        String.valueOf(item.getUndeliv_profit()),
                        String.valueOf(item.getHold_profit_f()),
                        String.valueOf(item.getMd_before_fund_in()),
                        String.valueOf(item.getMd_before_fund_out()),
                        String.valueOf(item.getSocrt_openfee()),
                        String.valueOf(item.getSettlementfee()),
                        String.valueOf(item.getStrike_credit()),
                        String.valueOf(item.getStrikeactreceivsum()),
                        String.valueOf(item.getStrikeactpaysum()),
                        String.valueOf(item.getDepositbytrade()),
                        String.valueOf(item.getFund_impawn_chg()),
                        String.valueOf(item.getLastdepositbytrade()),
                        String.valueOf(item.getClose_profit_f()),
                        String.valueOf(item.getLastfundmortgageout()),
                        String.valueOf(item.getLastfundmortgagein()),
                        String.valueOf(item.getDeposit_bytrade()),
                        String.valueOf(item.getYes_deposit_bytrade()),
                        String.valueOf(item.getYes_fund_impawn_out()),
                        String.valueOf(item.getYes_fund_impawn_in()),
                        String.valueOf(item.getFund_impawn_market_margin()),
                        String.valueOf(item.getSett_type()),
                        String.valueOf(item.getTotal_transfee()),
                        String.valueOf(item.getTotal_market_transfee()),
                        String.valueOf(item.getExecute_transfee()),
                        String.valueOf(item.getMarket_execute_transfee()),
                        String.valueOf(item.getClient_no()),
                        String.valueOf(item.getBuyfund()),
                        String.valueOf(item.getSelfund()),
                        String.valueOf(item.getId()),
                        String.valueOf(item.getEncrypt_lable())
                };
                String[] getRidNull = replaceNullWithEmpty(row);
                writer.writeNext(getRidNull);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("共写入" + count + "条数据。");
        log.info("生成路径"+filPath);
    }

    // ================== T_CLOSE_DETAIL, T_DELIVERY, T_EXECUTE_RESULT, T_HOLD_BALANCE 等大数据量的表导出逻辑与T_CLIENT_SETT相似，都实现了分批全量导出和增量导出，后续省略注释 ==================
    public String[] t_close_detail_header(){
        String[] header = {
                "busi_date",
                "brokers_id",
                "market_id",
                "done_no",
                "open_done_no",
                "open_date",
                "open_price",
                "close_price",
                "amount",
                "transfee",
                "hand_amount",
                "fund_account_id",
                "security_id",
                "trade_account_id",
                "entrust_type",
                "yes_sett_price",
                "sett_price",
                "close_profit_bydate",
                "close_profit_bytrade",
                "market_transfee",
                "client_id",
                "trade_prop",
                "money_type",
                "ds_date",
                "data_source",
                "branch_id",
                "trade_type",
                "bs_direction",
                "trade_order",
                "seat_no",
                "deliv_date",
                "zr_flag",
                "dc_flag",
                "security_code",
                "exch_rate_1t",
                "option_type",
                "client_name",
                "offset_type",
                "client_no",
                "product_id",
                "id",
                "encrypt_lable"
        };
        return header;
    }
    public void t_close_detail(int type,String FileName,String startDate,String endDate) throws Exception {
        String[] header = t_close_detail_header();

        if(type==1){
            int count = tcdMapper.getCount();
            int start=1,batch_size=10000;
            for(int i=start;i<count;i+=batch_size){
                int n=i,m=i+batch_size-1;

                System.out.print(n+"-"+m+"批次写入"+FileName+"中...");

                List<t_close_detail> list = tcdMapper.getAll(n,m);

                t_close_detail_writeData(FileName,list);
            }
        }
        else if(type==2){

            List<t_close_detail> increment = tcdMapper.getIncrement(startDate,endDate);
            if(increment.size()!=0){
                t_close_detail_writeData(FileName,increment);
            }

        }
    }
    public void t_close_detail_writeData(String FilePath,List<t_close_detail> list) throws Exception{
        String filPath = FilePath;
        int count = 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {

            // 遍历所有t_close_detail对象，并将它们的值写入CSV文件
            for (t_close_detail item : list) {
                if(item==null){
                    break;
                }
                String[] row = {
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getBrokers_id()),
                        String.valueOf(item.getMarket_id()),
                        String.valueOf(item.getDone_no()),
                        String.valueOf(item.getOpen_done_no()),
                        String.valueOf(item.getOpen_date()),
                        String.valueOf(item.getOpen_price()),
                        String.valueOf(item.getClose_price()),
                        String.valueOf(item.getAmount()),
                        String.valueOf(item.getTransfee()),
                        String.valueOf(item.getHand_amount()),
                        String.valueOf(item.getFund_account_id()),
                        String.valueOf(item.getSecurity_id()),
                        String.valueOf(item.getTrade_account_id()),
                        String.valueOf(item.getEntrust_type()),
                        String.valueOf(item.getYes_sett_price()),
                        String.valueOf(item.getSett_price()),
                        String.valueOf(item.getClose_profit_bydate()),
                        String.valueOf(item.getClose_profit_bytrade()),
                        String.valueOf(item.getMarket_transfee()),
                        String.valueOf(item.getClient_id()),
                        String.valueOf(item.getTrade_prop()),
                        String.valueOf(item.getMoney_type()),
                        String.valueOf(item.getDs_date()),
                        String.valueOf(item.getData_source()),
                        String.valueOf(item.getBranch_id()),
                        String.valueOf(item.getTrade_type()),
                        String.valueOf(item.getBs_direction()),
                        String.valueOf(item.getTrade_order()),
                        String.valueOf(item.getSeat_no()),
                        String.valueOf(item.getDeliv_date()),
                        String.valueOf(item.getZr_flag()),
                        String.valueOf(item.getDc_flag()),
                        String.valueOf(item.getSecurity_code()),
                        String.valueOf(item.getExch_rate_1t()),
                        String.valueOf(item.getOption_type()),
                        String.valueOf(item.getClient_name()),
                        String.valueOf(item.getOffset_type()),
                        String.valueOf(item.getClient_no()),
                        String.valueOf(item.getProduct_id()),
                        String.valueOf(item.getId()),
                        String.valueOf(item.getEncrypt_lable())
                };
                String[] getRidNull = replaceNullWithEmpty(row);
                writer.writeNext(getRidNull);
                count++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("共写入" + count + "条数据。");
        log.info("生成路径"+filPath);
    }



    public  String[] t_delivery_header() {
        String[] header ={
                "busi_date",
                "client_id",
                "security_id",
                "trade_type",
                "market_id",
                "fund_account_id",
                "trade_account_id",
                "delivery_amount",
                "delivery_price",
                "undelivery_amount",
                "hand_amount",
                "pay_amount",
                "brand_code",
                "warehouse",
                "premium_rate",
                "premium",
                "remark",
                "transfee",
                "market_transfee",
                "cost",
                "forzen_amount",
                "delivery_profit_bydate",
                "delivery_profit_bytrade",
                "margin",
                "market_margin",
                "expire_date",
                "delivery_type",
                "sett_date",
                "delivery_date",
                "brokers_id",
                "trade_prop",
                "money_type",
                "ds_date",
                "data_source",
                "branch_id",
                "bs_direction",
                "deliv_no",
                "seat_no",
                "done_amt",
                "remain_amt",
                "settle_price",
                "settle_amt",
                "other_fee",
                "act_amt",
                "invoice_no",
                "commi_amt",
                "commi_rate",
                "remain_rate",
                "margin_price",
                "exch_commi_amt",
                "exch_commi_rate",
                "frz_margin",
                "frz_exch_margin",
                "security_code",
                "exch_rate_1t",
                "exch_rate",
                "option_type",
                "client_no",
                "product_id",
                "settlement_date",
                "id",
                "encrypt_lable"
        };
        return header;
    }
    public void t_delivery(int type,String FileName,String startDate,String endDate) throws Exception {
        String[] header = t_delivery_header();

        if(type==1){
            int count = tdMapper.getCount();
            int start=1,batch_size=10000;
            for(int i=start;i<count;i+=batch_size){
                int n=i,m=i+batch_size-1;

                System.out.print(n+"-"+m+"批次写入"+FileName+"中...");

                List<t_delivery> list = tdMapper.getAll(n,m);

                t_delivery_writeData(FileName,list);
            }
        }
        else if(type==2){
            List<t_delivery> increment = tdMapper.getIncrement(startDate,endDate);
            if(increment.size()!=0){
                t_delivery_writeData(FileName,increment);
            }
        }
    }
    public void t_delivery_writeData(String FilePath,List<t_delivery> list) throws Exception{
        String filPath = FilePath;
        int count = 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {

            for (t_delivery item : list) {
                if(item==null){
                    break;
                }
                String[] row = {
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getClient_id()),
                        String.valueOf(item.getSecurity_id()),
                        String.valueOf(item.getTrade_type()),
                        String.valueOf(item.getMarket_id()),
                        String.valueOf(item.getFund_account_id()),
                        String.valueOf(item.getTrade_account_id()),
                        String.valueOf(item.getDelivery_amount()),
                        String.valueOf(item.getDelivery_price()),
                        String.valueOf(item.getUndelivery_amount()),
                        String.valueOf(item.getHand_amount()),
                        String.valueOf(item.getPay_amount()),
                        String.valueOf(item.getBrand_code()),
                        String.valueOf(item.getWarehouse()),
                        String.valueOf(item.getPremium_rate()),
                        String.valueOf(item.getPremium()),
                        String.valueOf(item.getRemark()),
                        String.valueOf(item.getTransfee()),
                        String.valueOf(item.getMarket_transfee()),
                        String.valueOf(item.getCost()),
                        String.valueOf(item.getForzen_amount()),
                        String.valueOf(item.getDelivery_profit_bydate()),
                        String.valueOf(item.getDelivery_profit_bytrade()),
                        String.valueOf(item.getMargin()),
                        String.valueOf(item.getMarket_margin()),
                        String.valueOf(item.getExpire_date()),
                        String.valueOf(item.getDelivery_type()),
                        String.valueOf(item.getSett_date()),
                        String.valueOf(item.getDelivery_date()) ,
                        String.valueOf(item.getBrokers_id()),
                        String.valueOf(item.getTrade_prop()),
                        String.valueOf(item.getMoney_type()),
                        String.valueOf(item.getDs_date()),
                        String.valueOf(item.getData_source()),
                        String.valueOf(item.getBranch_id()),
                        String.valueOf(item.getBs_direction()),
                        String.valueOf(item.getDeliv_no()),
                        String.valueOf(item.getSeat_no()),
                        String.valueOf(item.getDone_amt()),
                        String.valueOf(item.getRemain_amt()),
                        String.valueOf(item.getSettle_price()),
                        String.valueOf(item.getSettle_amt()),
                        String.valueOf(item.getOther_fee()),
                        String.valueOf(item.getAct_amt()),
                        String.valueOf(item.getInvoice_no()),
                        String.valueOf(item.getCommi_amt()),
                        String.valueOf(item.getCommi_rate()),
                        String.valueOf(item.getRemain_rate()),
                        String.valueOf(item.getMargin_price()),
                        String.valueOf(item.getExch_commi_amt()),
                        String.valueOf(item.getExch_commi_rate()),
                        String.valueOf(item.getFrz_margin()),
                        String.valueOf(item.getFrz_exch_margin()),
                        String.valueOf(item.getSecurity_code()),
                        String.valueOf(item.getExch_rate_1t()),
                        String.valueOf(item.getExch_rate()),
                        String.valueOf(item.getOption_type()),
                        String.valueOf(item.getClient_no()),
                        String.valueOf(item.getProduct_id()),
                        String.valueOf(item.getSettlement_date()),
                        String.valueOf(item.getId()),
                        String.valueOf(item.getEncrypt_lable())
                };
                String[] getRidNull = replaceNullWithEmpty(row);

                writer.writeNext(getRidNull);

                count++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("共写入" + count + "条数据。");
        log.info("生成路径"+filPath);
    }


    public String[] t_execute_result_header() {
        // 创建一个包含所有成员变量名的数组，作为CSV的表头
        String[] header = {
                "busi_date",
                "client_id",
                "client_name",
                "execute_no",
                "market_id",
                "seat_no",
                "trade_account_id",
                "security_code",
                "security_id",
                "deliv_date",
                "strike_price",
                "option_type",
                "bs_direction",
                "trade_prop",
                "money_type",
                "strike_qty",
                "deliv_price",
                "optstrike_profit",
                "strikefee",
                "market_strikefee",
                "strikefee_rate",
                "strikefee_amt",
                "market_strikefee_rate",
                "market_strikefee_amt",
                "hand_amount",
                "note",
                "deliv_type",
                "ds_date",
                "data_source",
                "fund_account_id",
                "branch_id",
                "trade_type",
                "strikefrozenmargin",
                "exchstrikefrozenmargin",
                "strike_type",
                "client_no",
                "execute_transfee",
                "market_execute_transfee",
                "market_performfee",
                "performfee",
                "product_id",
                "id",
                "encrypt_lable"
        };
        return header;
    }
    public void t_execute_result(int type,String FileName,String startDate,String endDate) throws Exception {

        String[] header = t_execute_result_header();
        if(type==1){
            int count = terMapper.getCount();
            int start=1,batch_size=10000;
            for(int i=start;i<count;i+=batch_size){
                int n=i,m=i+batch_size-1;

                System.out.print(n+"-"+m+"批次写入"+FileName+"中...");

                List<t_execute_result> list = terMapper.getAll(n,m);

                t_execute_result_writeData(FileName,list);
            }
        }
        else if(type==2){
            List<t_execute_result> increment = terMapper.getIncrement(startDate,endDate);
            if(increment.size()!=0){
                t_execute_result_writeData(FileName,increment);
            }
        }
    }
    public void t_execute_result_writeData(String FilePath,List<t_execute_result> list) throws Exception{
        String filPath = FilePath;
        int count = 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {
            // 遍历所有t_delivery对象，并将它们的值写入CSV文件
            for (t_execute_result item : list) {
                if(item==null){
                    break;
                }
                String[] row = {
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getClient_id()),
                        String.valueOf(item.getClient_name()),
                        String.valueOf(item.getExecute_no()),
                        String.valueOf(item.getMarket_id()),
                        String.valueOf(item.getSeat_no()),
                        String.valueOf(item.getTrade_account_id()),
                        String.valueOf(item.getSecurity_code()),
                        String.valueOf(item.getSecurity_id()),
                        String.valueOf(item.getDeliv_date()),
                        String.valueOf(item.getStrike_price()),
                        String.valueOf(item.getOption_type()),
                        String.valueOf(item.getBs_direction()),
                        String.valueOf(item.getTrade_prop()),
                        String.valueOf(item.getMoney_type()),
                        String.valueOf(item.getStrike_qty()),
                        String.valueOf(item.getDeliv_price()),
                        String.valueOf(item.getOptstrike_profit()),
                        String.valueOf(item.getStrikefee()),
                        String.valueOf(item.getMarket_strikefee()),
                        String.valueOf(item.getStrikefee_rate()),
                        String.valueOf(item.getStrikefee_amt()),
                        String.valueOf(item.getMarket_strikefee_rate()),
                        String.valueOf(item.getMarket_strikefee_amt()),
                        String.valueOf(item.getHand_amount()),
                        String.valueOf(item.getNote()),
                        String.valueOf(item.getDeliv_type()),
                        String.valueOf(item.getDs_date()),
                        String.valueOf(item.getData_source()),
                        String.valueOf(item.getFund_account_id()),
                        String.valueOf(item.getBranch_id()),
                        String.valueOf(item.getTrade_type()),
                        String.valueOf(item.getStrikefrozenmargin()),
                        String.valueOf(item.getExchstrikefrozenmargin()),
                        String.valueOf(item.getStrike_type()),
                        String.valueOf(item.getClient_no()),
                        String.valueOf(item.getExecute_transfee()),
                        String.valueOf(item.getMarket_execute_transfee()),
                        String.valueOf(item.getMarket_performfee()),
                        String.valueOf(item.getPerformfee()),
                        String.valueOf(item.getProduct_id()),
                        String.valueOf(item.getId()),
                        String.valueOf(item.getEncrypt_lable())
                };

                String[] getRidNull = replaceNullWithEmpty(row);
                writer.writeNext(getRidNull);
                count++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("共写入" + count + "条数据。");
        log.info("生成路径"+filPath);
    }


    public String[] t_hold_balance_header() {
        // 创建一个包含所有成员变量名的数组，作为CSV的表头
        String[] header = {
                "busi_date",
                "brokers_id",
                "client_id",
                "fund_account_id",
                "trade_account_id",
                "branch_id",
                "security_id",
                "market_id",
                "enable_amount",
                "frozen_amount",
                "cost",
                "market_value",
                "data_source",
                "ds_date",
                "begin_market_value",
                "begin_amount",
                "hand_amount",
                "open_amount",
                "open_money",
                "close_amount",
                "close_money",
                "hold_amount",
                "hold_money",
                "hold_profit",
                "today_profit",
                "sett_price",
                "trade_type",
                "hold_profit_bydate",
                "hold_profit_bytrade",
                "close_profit",
                "close_profit_bydate",
                "close_profit_bytrade",
                "margin",
                "market_margin",
                "transfee",
                "market_transfee",
                "money_type",
                "bs_direction",
                "settfee",
                "market_settfee",
                "holdmovefee",
                "market_holdmovefee",
                "delivfee",
                "market_delivfee",
                "trade_prop",
                "opt_marketvalue",
                "security_code",
                "product_id",
                "done_amt",
                "done_sum",
                "close_amount_today",
                "close_money_today",
                "client_no",
                "optstrike_profit",
                "id",
                "encrypt_lable"
        };
        return header;
    }
    public void t_hold_balance(int type,String FileName,String startDate,String endDate) throws Exception {
        String[] header = t_hold_balance_header();

        if(type==1){
            int count = thbMapper.getCount();
            int start=1,batch_size=10000;
            for(int i=start;i<count;i+=batch_size){
                int n=i,m=i+batch_size-1;

                System.out.print(n+"-"+m+"批次写入"+FileName+"中...");


                List<t_hold_balance> list = thbMapper.getAll(n,m);

                t_hold_balance_writeData(FileName,list);
            }
        }
        else if(type==2){
            List<t_hold_balance> increment = thbMapper.getIncrement(startDate,endDate);
            if(increment.size()!=0){
                t_hold_balance_writeData(FileName,increment);
            }

        }
    }
    public void t_hold_balance_writeData(String FilePath,List<t_hold_balance> list) throws Exception{
        String filPath = FilePath;
        int count = 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filPath,true),'\u0001',ICSVWriter.NO_QUOTE_CHARACTER,ICSVWriter.DEFAULT_ESCAPE_CHARACTER,ICSVWriter.DEFAULT_LINE_END)) {

            // 遍历所有t_execute_result对象，并将它们的值写入CSV文件
            for (t_hold_balance item : list) {
                if(item==null){
                    break;
                }
                String[] row = {
                        String.valueOf(item.getBusi_date()),
                        String.valueOf(item.getBrokers_id()),
                        String.valueOf(item.getClient_id()),
                        String.valueOf(item.getFund_account_id()),
                        String.valueOf(item.getTrade_account_id()),
                        String.valueOf(item.getBranch_id()),
                        String.valueOf(item.getSecurity_id()),
                        String.valueOf(item.getMarket_id()),
                        String.valueOf(item.getEnable_amount()),
                        String.valueOf(item.getFrozen_amount()),
                        String.valueOf(item.getCost()),
                        String.valueOf(item.getMarket_value()),
                        String.valueOf(item.getData_source()),
                        String.valueOf(item.getDs_date()),
                        String.valueOf(item.getBegin_market_value()),
                        String.valueOf(item.getBegin_amount()),
                        String.valueOf(item.getHand_amount()),
                        String.valueOf(item.getOpen_amount()),
                        String.valueOf(item.getOpen_money()),
                        String.valueOf(item.getClose_amount()),
                        String.valueOf(item.getClose_money()),
                        String.valueOf(item.getHold_amount()),
                        String.valueOf(item.getHold_money()),
                        String.valueOf(item.getHold_profit()),
                        String.valueOf(item.getToday_profit()),
                        String.valueOf(item.getSett_price()),
                        String.valueOf(item.getTrade_type()),
                        String.valueOf(item.getHold_profit_bydate()),
                        String.valueOf(item.getHold_profit_bytrade()),
                        String.valueOf(item.getClose_profit()),
                        String.valueOf(item.getClose_profit_bydate()),
                        String.valueOf(item.getClose_profit_bytrade()),
                        String.valueOf(item.getMargin()),
                        String.valueOf(item.getMarket_margin()),
                        String.valueOf(item.getTransfee()),
                        String.valueOf(item.getMarket_transfee()),
                        String.valueOf(item.getMoney_type()),
                        String.valueOf(item.getBs_direction()),
                        String.valueOf(item.getSettfee()),
                        String.valueOf(item.getMarket_settfee()),
                        String.valueOf(item.getHoldmovefee()),
                        String.valueOf(item.getMarket_holdmovefee()),
                        String.valueOf(item.getDelivfee()),
                        String.valueOf(item.getMarket_delivfee()),
                        String.valueOf(item.getTrade_prop()),
                        String.valueOf(item.getOpt_marketvalue()),
                        String.valueOf(item.getSecurity_code()),
                        String.valueOf(item.getProduct_id()),
                        String.valueOf(item.getDone_amt()),
                        String.valueOf(item.getDone_sum()),
                        String.valueOf(item.getClose_amount_today()),
                        String.valueOf(item.getClose_money_today()),
                        String.valueOf(item.getClient_no()),
                        String.valueOf(item.getOptstrike_profit()),
                        String.valueOf(item.getId()),
                        String.valueOf(item.getEncrypt_lable())
                };
                String[] getRidNull = replaceNullWithEmpty(row);

                writer.writeNext(getRidNull);

                count++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("共写入" + count + "条数据。");
        log.info("生成路径"+filPath);
    }


    /**
     * 工具方法：将字符串数组中的 "null" 字符串替换为空字符串 ""
     *
     * @param arr 原始字符串数组。
     * @return 处理后的字符串数组。
     */
    public String[] replaceNullWithEmpty(String[] arr) {
        return Arrays.stream(arr)
                .map(x -> x!= null && x.equals("null")? "" : x)
                .toArray(String[]::new);
    }
}
