package org.example.processserver.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2024/11/4
 * @Description
 */
@Component
@Data
public class ads_ptad {
    @CsvBindByName(column = "client_id",required = true)
    String client_id;//客户ID

        @CsvBindByName(column = "product_id")
        String product_id;//产品id
        @CsvBindByName(column = "busi_date")
        String busi_date;//业务日期
        @CsvBindByName(column = "rncjl")
        Double rncjl;//日内成交量
        @CsvBindByName(column = "zcjl")
        Double zcjl;//总成交量
        @CsvBindByName(column = "cje")
        Double cje;//成交额
        @CsvBindByName(column = "ccyk")
        Double ccyk;//持仓盈亏
        @CsvBindByName(column = "jyyk")
        Double jyyk;//交易盈亏
        @CsvBindByName(column = "sxf")
        Double sxf;//手续费

    public ads_ptad() {
    }

    public ads_ptad(String client_id, String product_id, String busi_date, Double rncjl, Double zcjl, Double cje, Double ccyk, Double jyyk, Double sxf) {
        this.client_id = client_id;
        this.product_id = product_id;
        this.busi_date = busi_date;
        this.rncjl = rncjl;
        this.zcjl = zcjl;
        this.cje = cje;
        this.ccyk = ccyk;
        this.jyyk = jyyk;
        this.sxf = sxf;
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

    public String getBusi_date() {
        return busi_date;
    }

    public void setBusi_date(String busi_date) {
        this.busi_date = busi_date;
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

    @Override
    public String toString() {
        return "ads_ptad{" +
                "client_id='" + client_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", busi_date='" + busi_date + '\'' +
                ", rncjl='" + rncjl + '\'' +
                ", zcjl='" + zcjl + '\'' +
                ", cje='" + cje + '\'' +
                ", ccyk='" + ccyk + '\'' +
                ", jyyk='" + jyyk + '\'' +
                ", sxf='" + sxf + '\'' +
                '}';
    }

    public Object setData(String[] str, ads_ptad adsPtad) {
        if (str[0] != "") {
            adsPtad.setClient_id(str[0]);
        }
        if (str[1] != "") {
            adsPtad.setProduct_id(str[1]);
        }
        if (str[2] != "") {
            adsPtad.setBusi_date(str[2]);
        }
        if(str[3] != null && !str[3].trim().isEmpty()) {
            adsPtad.setRncjl(Double.valueOf(str[3].trim()));
        }
        if (str[4] != null && !str[4].trim().isEmpty()) {
            adsPtad.setZcjl(Double.valueOf(str[4].trim()));
        }
        if (str[5] != null && !str[5].trim().isEmpty()) {
            adsPtad.setCje(Double.valueOf(str[5].trim()));
        }
        if (str[6] != null && !str[6].trim().isEmpty()) {
            adsPtad.setCcyk(Double.valueOf(str[6].trim()));
        }
        if (str[7] != null && !str[7].trim().isEmpty()) {
            adsPtad.setJyyk(Double.valueOf(str[7].trim()));
        }
        if (str[8] != null && !str[8].trim().isEmpty()) {
            adsPtad.setSxf(Double.valueOf(str[8].trim()));
        }
        
        return adsPtad;
    }
}
