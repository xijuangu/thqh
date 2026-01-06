package com.org.bddsserver.service;

import com.org.bddsserver.dto.response.DataCompareResponse;
import com.org.bddsserver.dto.request.DateRequest;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description
 */
public interface IndicatorCompareService {
    DataCompareResponse getDataCompareResponse(DateRequest dateRequest);
}
