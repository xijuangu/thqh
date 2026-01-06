package com.logdemo.lightserver.Controller;

import com.logdemo.lightserver.Mapper.responseMapper;
import com.logdemo.lightserver.dto.responseDto;
import com.logdemo.lightserver.pojo.data;
import com.logdemo.lightserver.util.Calculate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 响应数据控制层
 * 负责提供指标计算结果查询、数据列表查询等HTTP接口
 *
 * @author py.Lu（
 * @version 1.0.0
 * @since 2020-04-07 14:39:11
 */

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:8080") /// 开发环境跨域：仅允许本地8080前端服务请求，生产环境需替换为实际前端域名
public class responseController {

    @Autowired
    private Calculate calculate;

    @Autowired
    private responseMapper responsemapper;

    /**
     * 按指标查询表格数据
     * 根据传入的指标名称，调用计算工具类获取数据库中最晚日期的当期涨幅、5日涨幅、当期利润等，封装为响应DTO返回
     * 用于前端展示指标统计表格数据
     *
     * @param indicator 指标名称
     * @return responseDto 指标计算结果DTO，包含当前日期DPR、五期DPR、CP及其他基础字段
     */
    @GetMapping("/api/table")
    public responseDto test(@RequestParam String indicator) {
        // 日志记录：标记指标计算开始，便于问题排查时定位请求上下文
        log.info("------------------------- 计算" + indicator+"表格数据 -------------------------");
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 1. 求最晚日期、上一最晚日期、最晚日期的当期涨幅、最晚日期的理论成本价、第二最晚日期的理论成本价
        responseDto responsedto = calculate.calculateDPR(indicator, currentDate.toString());

        // 2. 计算基于最晚日期的5日涨幅
        String fiveDPR = calculate.calculatefiveDPR(indicator, currentDate.toString());

        // 3. 计算最晚日期的当期利润
        String CP = calculate.calculateCP(indicator, currentDate.toString());

        responsedto.setFiveDPR(fiveDPR);
        responsedto.setCP(CP);

        return responsedto;
    }



    /**
     * 查询所有指标的在所有日期的理论价，用于绘制曲线图
     *
     * @param
     * @return List<data> 返回所有指标的在所有日期的理论价
     */
    @GetMapping("/api/getAll")
    public List<data> getAll(){
        return responsemapper.selectAllspodumene();
    }

    /**
     * 查询数据库中最晚日期的各指标的理论价数据，用于指标卡片展示
     *
     * @param
     * @return List<data> 返回所有指标的在所有日期的理论价
     */
    @GetMapping("/api/getNewOne")
    public List<data> getNewOne(){
        return responsemapper.selectNewOne();
    }

}
