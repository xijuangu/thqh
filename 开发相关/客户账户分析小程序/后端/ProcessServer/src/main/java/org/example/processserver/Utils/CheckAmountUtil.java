package org.example.processserver.Utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.processserver.mapper.db1.*;
import org.example.processserver.mapper.db2.*;
import org.example.processserver.pojo.SyncStateTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author PY.Lu
 * @date 2024/10/25
 * @Description
 */
@Slf4j
@Service
public class CheckAmountUtil {

    // ================== 数据库Mapper依赖注入 ==================
    // Mysql库(M)
    // Oracle库(O)

    @Autowired
    private t_client_settMapper_M tcsMapper1;
    @Autowired
    private t_client_settMapper_O tcsMapper2;

    @Autowired
    private t_close_detailMapper_M tcdMapper1;
    @Autowired
    private t_close_detailMapper_O tcdMapper2;

    @Autowired
    private t_deliveryMapper_M tdMapper1;
    @Autowired
    private t_deliveryMapper_O tdMapper2;

    @Autowired
    private t_execute_resultMapper_M terMapper1;
    @Autowired
    private t_execute_resultMapper_O terMapper2;

    @Autowired
    private t_hold_balanceMapper_M thbMapper1;
    @Autowired
    private t_hold_balanceMapper_O thbMapper2;


    @Autowired
    private t_doneMapper_M tdoneMapper1;
    @Autowired
    private t_doneMapper_O tdoneMapper2;

    @Autowired
    private t_entrustMapper_M tentrustMapper1;
    @Autowired
    private t_entrustMapper_O tentrustMapper2;

    @Autowired
    private t_fund_jourMapper_M tfjMapper1;
    @Autowired
    private t_fund_jourMapper_O tfjMapper2;

    @Autowired
    private t_hold_detailMapper_M thdMapper1;
    @Autowired
    private t_hold_detailMapper_O thdMapper2;


    @Autowired
    private t_marketMapper_M tmarketMapper1;
    @Autowired
    private t_marketMapper_O tmarketMapper2;


    @Autowired
    private t_productMapper_M tproductMapper1;
    @Autowired
    private t_productMapper_O tproductMapper2;


    @Autowired
    private t_pub_dateMapper_M tpubdateMapper1;
    @Autowired
    private t_pub_dateMapper_O tpubdateMapper2;


    @Autowired
    private t_sercurityMapper_M tsercurityMapper1;
    @Autowired
    private t_sercurityMapper_O tsercurityMapper2;


    @Autowired
    private ads_cfsMapper ads_cfsM;

    @Autowired
    private SyncStateTableMapper syncMapper;

    /**
     * 执行项目Mysql库(M)与CRM oracle库(O)之间的数据量校验。
     * <p>
     * 根据数据库中配置的待校验表列表，动态地选择对应的Mapper，并调用通用校验模板进行校验。
     *
     * @param type 校验类型。1表示全量校验，2表示增量校验。
     * @return 固定返回0，表示方法执行完成。实际的校验结果通过数据库SyncStateTable表（同步状态表）查看。
     */
    public int CheckAmount(int type) {
        log.info("【数据同步检查】 ====================== 主库同步数据数量检查开始： ======================");

        // 从同步状态表中获取所有需要检查的表的配置信息
        List<SyncStateTable> syncStateTables = syncMapper.searchAll();

        // 创建Mapper映射表，用于将表名与对应的Mapper实例关联起来
        Map<String, CommonMapper_M> mapper_M = new HashMap<>();
        Map<String, CommonMapper_O> mapper_O = new HashMap<>();

        // 遍历配置列表，将需要检查的表（checkFlag=1）的Mapper存入映射表
        for (SyncStateTable syncStateTable : syncStateTables) {
            if (syncStateTable.getCheckFlag() == 1) {
                // 使用switch语句将表名与具体的Mapper实现进行绑定
                switch (syncStateTable.getTableName()) {
                    case "T_CLIENT_SETT":
                        mapper_M.put("T_CLIENT_SETT", tcsMapper1);
                        mapper_O.put("T_CLIENT_SETT", tcsMapper2);
                        break;
                    case "T_CLOSE_DETAIL":
                        mapper_M.put("T_CLOSE_DETAIL", tcdMapper1);
                        mapper_O.put("T_CLOSE_DETAIL", tcdMapper2);
                        break;
                    case "T_DELIVERY":
                        mapper_M.put("T_DELIVERY", tdMapper1);
                        mapper_O.put("T_DELIVERY", tdMapper2);
                        break;
                    case "T_EXECUTE_RESULT":
                        mapper_M.put("T_EXECUTE_RESULT", terMapper1);
                        mapper_O.put("T_EXECUTE_RESULT", terMapper2);
                        break;
                    case "T_PRODUCT":
                        mapper_M.put("T_PRODUCT", tproductMapper1);
                        mapper_O.put("T_PRODUCT", tproductMapper2);
                        break;
                    case "T_HOLD_BALANCE":
                        mapper_M.put("T_HOLD_BALANCE", thbMapper1);
                        mapper_O.put("T_HOLD_BALANCE", thbMapper2);
                        break;
                    case "T_PUB_DATE":
                        mapper_M.put("T_PUB_DATE", tpubdateMapper1);
                        mapper_O.put("T_PUB_DATE", tpubdateMapper2);
                        break;
                    case "T_DONE":
                        mapper_M.put("T_DONE", tdoneMapper1);
                        mapper_O.put("T_DONE", tdoneMapper2);
                        break;
                    case "T_ENTRUST":
                        mapper_M.put("T_ENTRUST", tentrustMapper1);
                        mapper_O.put("T_ENTRUST", tentrustMapper2);
                        break;
                    case "T_FUND_JOUR":
                        mapper_M.put("T_FUND_JOUR", tfjMapper1);
                        mapper_O.put("T_FUND_JOUR", tfjMapper2);
                        break;
                    case "T_HOLD_DETAIL":
                        mapper_M.put("T_HOLD_DETAIL", thdMapper1);
                        mapper_O.put("T_HOLD_DETAIL", thdMapper2);
                        break;
                    case "T_MARKET":
                        mapper_M.put("T_MARKET", tmarketMapper1);
                        mapper_O.put("T_MARKET", tmarketMapper2);
                        break;
                    case "T_SERCURITY":
                        mapper_M.put("T_SERCURITY", tsercurityMapper1);
                        mapper_O.put("T_SERCURITY", tsercurityMapper2);
                        break;
                    default:
                        break;
                }

            }
        }

        // 调用通用校验模板执行具体的校验逻辑
        check_template(type, mapper_M, mapper_O);
        log.info("【数据同步检查】 ====================== 主库同步数据数量检查结束： =========");
        return 0;

    }

    /**
     * 通用校验模板方法，封装了主备库对比的核心逻辑。
     *
     * @param type 校验类型 (1: 全量, 2: 增量)。
     * @param check_mapper 项目Mysql库(M)的Mapper映射表。
     * @param check_mapper_O CRM oracle库(O)的Mapper映射表。
     */
    private void check_template(int type, Map<String, CommonMapper_M> check_mapper, Map<String, CommonMapper_O> check_mapper_O) {
        // 使用Atomic类型确保在Lambda表达式中可以修改变量的值
        AtomicInteger count_mysql = new AtomicInteger();
        AtomicInteger count_oracle = new AtomicInteger();
        AtomicReference<String> check_type = new AtomicReference<>("");

        // 获取当前时间，用于记录校验时间戳
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 根据校验类型执行不同的逻辑
        if (type == 1) {
            // 全量校验逻辑
            check_mapper.forEach((key, value) -> {
                // 查询主库全量表行数
                count_mysql.set(value.getCount());
                // 查询备库全量表行数
                count_oracle.set(check_mapper_O.get(key).getCount_Oracle());
                check_type.set("全量");

                // 执行数量对比
                int i = equal_check(count_mysql.get(), count_oracle.get());

                // 更新同步状态表
                syncMapper.update(i, date, count_oracle.get(), count_mysql.get(), check_type.get(), key);

                // 打印详细的校验日志
                log.info("表" + key + check_type.get() + "数据量检查结果：" + i + "，主库数量：" + count_mysql.get() + "，备库数量：" + count_oracle.get());
            });

        } else if (type == 2) {
            // 增量校验逻辑
            check_mapper.forEach((key, value) -> {
                // 对于以下表不进行增量检查
                if(key.equals("T_SERCURITY")|| key.equals("T_MARKET")||key.equals("T_PUB_DATE")||key.equals("T_PRODUCT")){
                    return;
                }

                // 查询项目库增量数据行数
                count_mysql.set(value.Increment_getCount());
                // 查询CRM库增量数据行数
                count_oracle.set(check_mapper_O.get(key).Increment_getCount_Oracle());
                check_type.set("增量");

                // 执行数量对比
                int i = equal_check(count_mysql.get(), count_oracle.get());

                // 更新同步状态表
                syncMapper.update(i, date, count_oracle.get(), count_mysql.get(), check_type.get(), key);
                // 打印详细的校验日志
                log.info("表" + key + check_type.get() + "数据量检查结果：" + i + "，主库数量：" + count_mysql.get() + "，备库数量：" + count_oracle.get());
            });
        }

    }

    /**
     * 数量对比方法。
     *
     * @param count_mysql 项目Mysql库表数据量。
     * @param count_oracle CRM数据库表数据量。
     * @return 如果数量相等返回0，否则返回1。
     */
    public int equal_check(int count_mysql, int count_oracle) {
        if (count_mysql == count_oracle) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * 执行数仓(DW)文件与项目Mysql库之间的数据量校验。
     * <p>
     * 该方法会读取一个指定的JSON校验文件，该文件记录了每个接收的CSV数据文件应有的行数。
     * 然后，它会遍历数据目录下的所有CSV文件，将文件中的行数与备库中对应表的行数进行对比。
     *
     * @param MainPath 数据文件和校验文件所在的根目录。
     * @param DateYesterday 用于构造校验文件名的日期字符串（例如，"2024-10-24"）。
     */
    public void CheckAmount_DW(String MainPath, String DateYesterday) {
        log.info("【接收数据同步检查】 ====================== 数仓数据同步至项目库数据量检查开始： ======================");
        String CheckJsonName = MainPath + "/" + "check_" + DateYesterday + ".json";
        File file = new File(CheckJsonName);
        if (!file.exists()) {
            log.error("数仓数据同步至项目库数据量检查失败，check文件缺失！");
            return;
        }
        ObjectMapper om = new ObjectMapper();
        try {
            JsonFactory factory = om.getFactory();
            JsonParser jsonParser = factory.createParser(file);
            Map<?, ?> map = om.readValue(jsonParser, Map.class);

            File file_MainPath = new File(MainPath);
            File[] file_list = file_MainPath.listFiles();

            for (File f : file_list) {
                if (f.getName().equals("check_" + DateYesterday + ".json")) {
                    continue;
                }

                Map<?, ?> map_csv = (Map<?, ?>) map.get(f.getName());
                int rowCount_check = Integer.parseInt((String) map_csv.get("rowCount"));

                int rowCount_mysql = ads_cfsM.getCount_designatedTable(f.getName().replace("_" + DateYesterday + ".csv", ""));
                if (rowCount_check != rowCount_mysql) {
                    log.error("表" + f.getName() + "数据量与check文件不一致，请处理！");
                } else {
                    log.info("表" + f.getName() + "数据量与check文件一致，正常！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
