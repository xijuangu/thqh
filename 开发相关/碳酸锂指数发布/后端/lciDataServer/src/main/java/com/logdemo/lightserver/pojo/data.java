package com.logdemo.lightserver.pojo;

import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2025/3/6
 * @Description
 */
@Component
public class data {

    private Integer id;

    
    private String date;

    
    private Double spodumene;

    
    private Double Lg_Lepidolite;

    
    private Double Hg_Lepidolite;

    
    private Double LFP_Cathode;

    
    private Double LFP_BattPower;

    
    private Double NCM;

    
    private Double ECSpot;

    
    private Double CompoIndex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getSpodumene() {
        return spodumene;
    }

    public void setSpodumene(Double spodumene) {
        this.spodumene = spodumene;
    }

    public Double getLg_Lepidolite() {
        return Lg_Lepidolite;
    }

    public void setLg_Lepidolite(Double lg_Lepidolite) {
        Lg_Lepidolite = lg_Lepidolite;
    }

    public Double getHg_Lepidolite() {
        return Hg_Lepidolite;
    }

    public void setHg_Lepidolite(Double hg_Lepidolite) {
        Hg_Lepidolite = hg_Lepidolite;
    }

    public Double getLFP_Cathode() {
        return LFP_Cathode;
    }

    public void setLFP_Cathode(Double LFP_Cathode) {
        this.LFP_Cathode = LFP_Cathode;
    }

    public Double getLFP_BattPower() {
        return LFP_BattPower;
    }

    public void setLFP_BattPower(Double LFP_BattPower) {
        this.LFP_BattPower = LFP_BattPower;
    }

    public Double getNCM() {
        return NCM;
    }

    public void setNCM(Double NCM) {
        this.NCM = NCM;
    }

    public Double getEC_Spot() {
        return ECSpot;
    }

    public void setEC_Spot(Double ECSpot) {
        this.ECSpot = ECSpot;
    }

    public Double getComposite_Index() {
        return CompoIndex;
    }

    public void setComposite_Index(Double composite_Index) {
        this.CompoIndex = composite_Index;
    }

    @Override
    public String toString() {
        return "data{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", spodumene=" + spodumene +
                ", Lg_Lepidolite=" + Lg_Lepidolite +
                ", Hg_Lepidolite=" + Hg_Lepidolite +
                ", LFP_Cathode=" + LFP_Cathode +
                ", LFP_BattPower=" + LFP_BattPower +
                ", NCM=" + NCM +
                ", EC_Spot=" + ECSpot +
                ", Composite_Index=" + CompoIndex +
                '}';
    }
}
