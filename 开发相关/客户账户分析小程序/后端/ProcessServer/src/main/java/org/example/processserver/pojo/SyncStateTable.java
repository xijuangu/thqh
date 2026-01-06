package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2025/7/4
 * @Description
 */
@Component
@Data
public class SyncStateTable {
    Integer id;
    String tableName;
    String syncstate;
    Integer checkFlag;
    String checkTime;
    Integer sourceCount;
    Integer targetCount;
    String checkType;
}
