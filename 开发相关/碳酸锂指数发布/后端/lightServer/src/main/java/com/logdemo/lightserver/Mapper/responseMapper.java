package com.logdemo.lightserver.Mapper;

import com.logdemo.lightserver.dto.responseDto;
import com.logdemo.lightserver.pojo.data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface responseMapper {
    //    查询当日有效理论价和前一日有效理论价
    responseDto findCurrentValidPrice(String indicator, String date);
    responseDto findLastValidPrice(String indicator, String date);


    /*以下用于计算涨幅和利润*/

    //找有效当日理论价
    /*传入当天日期，基于当天日期查找有效当日理论价*/
    responseDto findValidTheoryPrice(String indicator, String date);

    //找有效上一日理论价
    /*传入有效当日理论价对应的日期，基于该日期查找有效上一日理论价*/
    responseDto findValidLastTheoryPrice(String indicator, String date);

    //找有效前4日理论价
    /*传入有效当日理论价对应的日期，基于该日期查找有效前4日理论价*/
    responseDto findValidFourDaysTheoryPrice(String indicator, String date);

    //找有效电碳现货价格
    /*传入当天日期，基于当天日期查找有效电碳现货价格*/
    responseDto findValidElecPrice(String indicator, String date);

    //查所有指标在所有日期的理论价
    List<data> selectAllspodumene();

    //查所有指标在最晚日期的理论价
    List<data> selectNewOne();
}
