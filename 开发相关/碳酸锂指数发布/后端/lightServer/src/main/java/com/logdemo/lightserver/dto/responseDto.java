package com.logdemo.lightserver.dto;

public class responseDto {
    private String name; // 指标名称
    private Double validPrice;//最晚日期的理论成本价
    private Double LastvalidPrice;//上一最晚日期的理论成本价
    private String currentdate;//最晚日期
    private String prevdate;//上一最晚日期


    private String DPR;//最晚日期的当日涨幅

    private String fiveDPR;//基于最晚日期的5日涨幅
    //当期利润
    private String CP;//最晚日期的当期利润

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getPrevdate() {
        return prevdate;
    }

    public void setPrevdate(String prevdate) {
        this.prevdate = prevdate;
    }


    public String getDPR() {
        return DPR;
    }

    public void setDPR(String DPR) {
        this.DPR = DPR;
    }

    public String getFiveDPR() {
        return fiveDPR;
    }

    public void setFiveDPR(String fiveDPR) {
        this.fiveDPR = fiveDPR;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public Double getValidPrice() {
        return validPrice;
    }

    public void setValidPrice(Double validPrice) {
        this.validPrice = validPrice;
    }

    public Double getLastvalidPrice() {
        return LastvalidPrice;
    }

    public void setLastvalidPrice(Double lastvalidPrice) {
        LastvalidPrice = lastvalidPrice;
    }

    @Override
    public String toString() {
        return "responseDto{" +
                "name='" + name + '\'' +
                ", validPrice=" + validPrice +
                ", LastvalidPrice=" + LastvalidPrice +
                ", currentdate='" + currentdate + '\'' +
                ", prevdate='" + prevdate + '\'' +
                ", DPR='" + DPR + '\'' +
                ", fiveDPR='" + fiveDPR + '\'' +
                ", CP='" + CP + '\'' +
                '}';
    }
}
