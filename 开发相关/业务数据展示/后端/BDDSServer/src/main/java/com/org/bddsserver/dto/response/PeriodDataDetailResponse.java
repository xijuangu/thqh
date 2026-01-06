package com.org.bddsserver.dto.response;

import com.org.bddsserver.dto.PeriodDataDetail;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/21
 * @Description
 */
@Data
public class PeriodDataDetailResponse {
    private String period; // 周期区间

    private List<PeriodDataDetail> periodDataDetailList; // 数据对比明细列表
}
