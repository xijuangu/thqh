package com.org.bddsserver.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description 某业务人员某指标某时期对比详情
 */
@Data
public class IndicatorPeriodCompareDetail {
    //人员基础信息
    private PersonnelInfo personnelInfo;

    private String[] client_type;
    private Double[] period1;
    private Double[] period2;
    private Double[] diff;

    private Map<String, Integer> map = new HashMap<>();

    public IndicatorPeriodCompareDetail() {
        this.personnelInfo = new PersonnelInfo();

        this.client_type = new String[]{"PER","IND","ORG"};
        this.period1 = new Double[]{0d,0d,0d};
        this.period2 = new Double[]{0d,0d,0d};
        this.diff = new Double[]{0d,0d,0d};

        this.map.put("PER", 0);
        this.map.put("IND", 1);
        this.map.put("ORG", 2);
    }

    public void setData(String client_type, Double p1, Double p2) {
        int index = this.map.get(client_type);
        this.period1[index] = p1;
        this.period2[index] = p2;
        this.diff[index] = p1 - p2;
    }
}
