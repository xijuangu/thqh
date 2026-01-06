package org.example.processserver.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2024/11/5
 * @Description ads_futures_product_trade_analysis 账户相关分析ADS
 */
@Component
@Data
public class ads_faa {
    @CsvBindByName(column = "client_id")
    String client_id;//客户ID
    @CsvBindByName(column = "period")
    String period;//期间
    @CsvBindByName(column = "begin_busi_date")
    String begin_busi_date;//起始交易日期
    @CsvBindByName(column = "end_busi_date")
    String end_busi_date;//结束交易日期
    @CsvBindByName(column = "busi_date")
    String busi_date;//交易日
    @CsvBindByName(column = "today_net_value")
    Double today_net_value;//当日净值
    @CsvBindByName(column = "cumulative_net_value")
    Double cumulative_net_value;//累计净值
    @CsvBindByName(column = "risk_level")
    Double risk_level;//风险度

//    @CsvBindByName(column = "market_deposit")
//    Double market_deposit;//市值权益

    @CsvBindByName(column = "dyn_end_rights")
    Double dyn_end_rights;//动态市值权益

    @CsvBindByName(column = "cumulative_gain_rate")
    Double cumulative_gain_rate;//累计收益率
    @CsvBindByName(column = "drawdown_rate")
    Double drawdown_rate;//回撤率
    @CsvBindByName(column = "trade_day_flag")
    Integer trade_day_flag;//交易日标志

    public ads_faa() {
    }

    public ads_faa(String client_id, String period, String begin_busi_date, String end_busi_date, String busi_date, Double today_net_value, Double cumulative_net_value, Double risk_level, Double dyn_end_rights, Double cumulative_gain_rate, Double drawdown_rate, Integer trade_day_flag) {
        this.client_id = client_id;
        this.period = period;
        this.begin_busi_date = begin_busi_date;
        this.end_busi_date = end_busi_date;
        this.busi_date = busi_date;
        this.today_net_value = today_net_value;
        this.cumulative_net_value = cumulative_net_value;
        this.risk_level = risk_level;
        this.dyn_end_rights = dyn_end_rights;
        this.cumulative_gain_rate = cumulative_gain_rate;
        this.drawdown_rate = drawdown_rate;
        this.trade_day_flag = trade_day_flag;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBegin_busi_date() {
        return begin_busi_date;
    }

    public void setBegin_busi_date(String begin_busi_date) {
        this.begin_busi_date = begin_busi_date;
    }

    public String getEnd_busi_date() {
        return end_busi_date;
    }

    public void setEnd_busi_date(String end_busi_date) {
        this.end_busi_date = end_busi_date;
    }

    public String getBusi_date() {
        return busi_date;
    }

    public void setBusi_date(String busi_date) {
        this.busi_date = busi_date;
    }

    public Double getToday_net_value() {
        return today_net_value;
    }

    public void setToday_net_value(Double today_net_value) {
        this.today_net_value = today_net_value;
    }

    public Double getCumulative_net_value() {
        return cumulative_net_value;
    }

    public void setCumulative_net_value(Double cumulative_net_value) {
        this.cumulative_net_value = cumulative_net_value;
    }

    public Double getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(Double risk_level) {
        this.risk_level = risk_level;
    }

    public Double getDyn_end_rights() {
        return dyn_end_rights;
    }

    public void setDyn_end_rights(Double dyn_end_rights) {
        this.dyn_end_rights = dyn_end_rights;
    }

    public Double getCumulative_gain_rate() {
        return cumulative_gain_rate;
    }

    public void setCumulative_gain_rate(Double cumulative_gain_rate) {
        this.cumulative_gain_rate = cumulative_gain_rate;
    }

    public Double getDrawdown_rate() {
        return drawdown_rate;
    }

    public void setDrawdown_rate(Double drawdown_rate) {
        this.drawdown_rate = drawdown_rate;
    }

    public Integer getTrade_day_flag() {
        return trade_day_flag;
    }

    public void setTrade_day_flag(Integer trade_day_flag) {
        this.trade_day_flag = trade_day_flag;
    }

    @Override
    public String toString() {
        return "ads_faa{" +
                "client_id='" + client_id + '\'' +
                ", period='" + period + '\'' +
                ", begin_busi_date='" + begin_busi_date + '\'' +
                ", end_busi_date='" + end_busi_date + '\'' +
                ", busi_date='" + busi_date + '\'' +
                ", today_net_value=" + today_net_value +
                ", cumulative_net_value=" + cumulative_net_value +
                ", risk_level=" + risk_level +
                ", dyn_end_rights=" + dyn_end_rights +
                ", cumulative_gain_rate=" + cumulative_gain_rate +
                ", drawdown_rate=" + drawdown_rate +
                ", trade_day_flag=" + trade_day_flag +
                '}';
    }

    public Object setData(String[] str, ads_faa adsFaa) {
        if(str[0] != ""){
            adsFaa.setClient_id(str[0]);
        }
        if(str[1] != ""){
            adsFaa.setPeriod(str[1]);
        }
        if(str[2] != ""){
            adsFaa.setBegin_busi_date(str[2]);
        }
        if(str[3] != ""){
            adsFaa.setEnd_busi_date(str[3]);
        }
        if(str[4] != ""){
            adsFaa.setBusi_date(str[4]);
        }
        if (str[5] != null && !str[5].trim().isEmpty()) {
            adsFaa.setToday_net_value(Double.parseDouble(str[5].trim()));
        }
        if(str[6] != null && !str[6].trim().isEmpty()){
            adsFaa.setCumulative_net_value(Double.parseDouble(str[6].trim()));
        }
        if(str[7] != null && !str[7].trim().isEmpty()){
            adsFaa.setRisk_level(Double.parseDouble(str[7].trim()));
        }
        if(str[8] != null && !str[8].trim().isEmpty()){
            adsFaa.setDyn_end_rights(Double.parseDouble(str[8].trim()));
        }
        if(str[9] != null && !str[9].trim().isEmpty()){
            adsFaa.setCumulative_gain_rate(Double.parseDouble(str[9].trim()));
        }
        if(str[10] != null && !str[10].trim().isEmpty()){
            adsFaa.setDrawdown_rate(Double.parseDouble(str[10].trim()));
        }
        if(str[11] != null && !str[11].trim().isEmpty()){
            adsFaa.setTrade_day_flag(Integer.parseInt(str[11].trim()));
        }
        return adsFaa;
    }
}
