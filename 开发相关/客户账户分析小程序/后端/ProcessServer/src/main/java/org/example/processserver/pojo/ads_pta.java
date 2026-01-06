package org.example.processserver.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2024/11/4
 * @Description ads_futures_product_trade_analysis 品种交易分析ADS
 */
@Component
@Data
public class ads_pta {
    @CsvBindByName(column = "client_id")
    String client_id;//客户ID
    @CsvBindByName(column = "product_id")
    String product_id;//产品ID
    @CsvBindByName(column = "period")
    String period;//期间
    @CsvBindByName(column = "begin_busi_date")
    String begin_busi_date;//起始交易日期
    @CsvBindByName(column = "end_busi_date")
    String end_busi_date;//结束交易日期
    @CsvBindByName(column = "rncjl")
    Double rncjl;//日内成交量
    @CsvBindByName(column = "zcjl")
    Double zcjl;//总成交量
    @CsvBindByName(column = "pcyk")
    Double pcyk;//平仓盈亏
    @CsvBindByName(column = "cje")
    Double cje;//成交额
    @CsvBindByName(column = "ccyk")
    Double ccyk;//持仓盈亏
    @CsvBindByName(column = "jyyk")
    Double jyyk;//交易盈亏
    @CsvBindByName(column = "sxf")
    Double sxf;//手续费
    @CsvBindByName(column = "jlr")
    Double jlr;//净利润


    public ads_pta() {
    }

    public ads_pta(String client_id, String product_id, String period, String begin_busi_date, String end_busi_date, Double rncjl, Double zcjl, Double pcyk, Double cje, Double ccyk, Double jyyk, Double sxf, Double jlr) {
        this.client_id = client_id;
        this.product_id = product_id;
        this.period = period;
        this.begin_busi_date = begin_busi_date;
        this.end_busi_date = end_busi_date;
        this.rncjl = rncjl;
        this.zcjl = zcjl;
        this.pcyk = pcyk;
        this.cje = cje;
        this.ccyk = ccyk;
        this.jyyk = jyyk;
        this.sxf = sxf;
        this.jlr = jlr;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public Double getRncjl() {
        return rncjl;
    }

    public void setRncjl(Double rncjl) {
        this.rncjl = rncjl;
    }

    public Double getZcjl() {
        return zcjl;
    }

    public void setZcjl(Double zcjl) {
        this.zcjl = zcjl;
    }

    public Double getPcyk() {
        return pcyk;
    }

    public void setPcyk(Double pcyk) {
        this.pcyk = pcyk;
    }

    public Double getCje() {
        return cje;
    }

    public void setCje(Double cje) {
        this.cje = cje;
    }

    public Double getCcyk() {
        return ccyk;
    }

    public void setCcyk(Double ccyk) {
        this.ccyk = ccyk;
    }

    public Double getJyyk() {
        return jyyk;
    }

    public void setJyyk(Double jyyk) {
        this.jyyk = jyyk;
    }

    public Double getSxf() {
        return sxf;
    }

    public void setSxf(Double sxf) {
        this.sxf = sxf;
    }

    public Double getJlr() {
        return jlr;
    }

    public void setJlr(Double jlr) {
        this.jlr = jlr;
    }

    @Override
    public String toString() {
        return "ads_pta{" +
                "client_id='" + client_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", period='" + period + '\'' +
                ", begin_busi_date='" + begin_busi_date + '\'' +
                ", end_busi_date='" + end_busi_date + '\'' +
                ", rncjl=" + rncjl +
                ", zcjl=" + zcjl +
                ", pcyk=" + pcyk +
                ", cje=" + cje +
                ", ccyk=" + ccyk +
                ", jyyk=" + jyyk +
                ", sxf=" + sxf +
                ", jlr=" + jlr +
                '}';
    }

    public Object setData(String[] str, ads_pta adsPta) {
        if (str[0] != "") {
            adsPta.setClient_id(str[0]);
        }
        if (str[1] != "") {
            adsPta.setProduct_id(str[1]);
        }
        if (str[2] != "") {
            adsPta.setPeriod(str[2]);
        }
        if (str[3] != "") {
            adsPta.setBegin_busi_date(str[3]);
        }
        if (str[4] != "") {
            adsPta.setEnd_busi_date(str[4]);
        }
        if (str[5] != null && !str[5].trim().isEmpty()) {
            adsPta.setRncjl(Double.parseDouble(str[5].trim()));
        }
        if (str[5] != null && !str[6].trim().isEmpty()) {
            adsPta.setZcjl(Double.parseDouble(str[6].trim()));
        }
        if (str[7] != null && !str[7].trim().isEmpty()) {
            adsPta.setPcyk(Double.parseDouble(str[7].trim()));
        }
        if (str[8] != null && !str[8].trim().isEmpty()) {
            adsPta.setCje(Double.parseDouble(str[8].trim()));
        }
        if (str[9] != null && !str[9].trim().isEmpty()) {
            adsPta.setCcyk(Double.parseDouble(str[9].trim()));
        }
        if (str[10] != null && !str[10].trim().isEmpty()) {
            adsPta.setJyyk(Double.parseDouble(str[10].trim()));
        }
        if (str[11] != null && !str[11].trim().isEmpty()) {
            adsPta.setSxf(Double.parseDouble(str[11].trim()));
        }
        if (str[12] != null && !str[12].trim().isEmpty()) {
            adsPta.setJlr(Double.parseDouble(str[12].trim()));
        }

        return adsPta;
    }
}
