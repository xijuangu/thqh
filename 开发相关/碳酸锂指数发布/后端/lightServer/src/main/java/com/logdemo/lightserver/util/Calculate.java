package com.logdemo.lightserver.util;

import com.logdemo.lightserver.Mapper.responseMapper;
import com.logdemo.lightserver.dto.responseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Calculate {
    @Autowired
    private responseMapper responseM;
    //计算当日涨幅，还需要负责返回：工艺名、数据库中最新日期 、数据库中第二最新日期、最新日期对应的理论成本价、第二最新日期对应的理论成本价、当日涨幅
    //date由Controller指定
    public responseDto calculateDPR(String indicator, String date) {
        log.info("------------------------- 计算{}当日涨幅 -------------------------", indicator);

        responseDto responsedto = new responseDto();//避免创建多个对象

        // 获取当日理论成本价（不论有效与否）
        responsedto = responseM.findCurrentValidPrice(indicator, date);
        Double todayTheoryPrice = responsedto.getValidPrice();
        String todayTheoryDate = responsedto.getCurrentdate();
        log.info("------------------------- {}当日{}理论成本价:{} -------------------------", indicator, todayTheoryDate,todayTheoryPrice);
        // 获取前一日日期和前一日理论成本价
        responsedto = responseM.findLastValidPrice(indicator,  date);
        String LastDate = responsedto.getCurrentdate();
        Double LastTheoryPrice = responsedto.getValidPrice();
        log.info("------------------------- {}前一日{}理论成本价:{} -------------------------", indicator, LastDate,LastTheoryPrice);


        //获取有效当日理论成本价
        responsedto = responseM.findValidTheoryPrice(indicator, date);
        Double CurrentvalidTheoryPrice = responsedto.getValidPrice();
        String CurrentvalidTheoryDate = responsedto.getCurrentdate();
        log.info("------------------------- {}当前有效理论成本价:{},对应日期:{} -------------------------", indicator, CurrentvalidTheoryPrice,CurrentvalidTheoryDate);

        //获取有效前一日理论成本价
        responsedto = responseM.findValidLastTheoryPrice(indicator, date);
        Double LastvalidTheoryPrice = responsedto.getValidPrice();
        String LastvalidTheoryDate = responsedto.getCurrentdate();
        log.info("------------------------- {}上一有效理论成本价:{},对应日期:{} -------------------------", indicator, LastvalidTheoryPrice,LastvalidTheoryDate);


        //计算当日涨幅
        Double increase = CurrentvalidTheoryPrice / LastvalidTheoryPrice - 1;
        log.info("------------------------- {}当日涨幅:{} ---------", indicator, increase);

        //封装返回数据
        responseDto responsedto3 = new responseDto();
        responsedto3.setName(indicator);//工艺名
        //向下取整
        responsedto3.setCurrentdate(todayTheoryDate);//数据库中最新日期
        responsedto3.setPrevdate(LastDate);//数据库中第二最新日期
        responsedto3.setValidPrice(Math.floor(todayTheoryPrice));//最新日期对应的理论成本价
        responsedto3.setLastvalidPrice(Math.floor(LastTheoryPrice));//第二最新日期对应的理论成本价
        responsedto3.setDPR(String.format("%.2f", increase * 100));//当日涨幅

        return responsedto3;
    }

    //计算五日涨幅
    public String calculatefiveDPR(String indicator, String date) {
        log.info("------------------------- 计算{}五日涨幅 ---------", indicator);

        responseDto responsedto = new responseDto();//避免创建多个对象

        //不再重复获取当日和上一日理论成本价（不论有效与否）
        //计算五日涨幅
        //获取有效当日理论成本价
        responsedto = responseM.findValidTheoryPrice(indicator, date);
        Double CurrentvalidTheoryPrice = responsedto.getValidPrice();
        String CurrentvalidTheoryDate = responsedto.getCurrentdate();
        log.info("------------------------- {}当前有效理论成本价:{},对应日期:{} ---------", indicator, CurrentvalidTheoryPrice,CurrentvalidTheoryDate);

        //获取有效前五日理论成本价
        responsedto = responseM.findValidFourDaysTheoryPrice(indicator, date);
        Double LastvalidFourDaysTheoryPrice = responsedto.getValidPrice();
        String LastvalidFourDaysTheoryDate = responsedto.getCurrentdate();
        log.info("---------- {}上五日有效理论成本价:{},对应日期:{} ---------", indicator, LastvalidFourDaysTheoryPrice,LastvalidFourDaysTheoryDate);
        //计算五日涨幅
        Double increase = CurrentvalidTheoryPrice / LastvalidFourDaysTheoryPrice - 1;
        log.info("---------- {}五日涨幅:{} ---------", indicator, increase);

        return String.format("%.2f", increase * 100);

    }

    //计算当期利润
    public String calculateCP(String indicator, String date) {
        log.info("------------------------- 计算{}当期利润 ---------", indicator);

        responseDto responsedto = new responseDto();//避免创建多个对象

        //不再重复获取当日和上一日理论成本价（不论有效与否）
        //计算当期利润
        //获取有效当日理论成本价
        responsedto = responseM.findValidTheoryPrice(indicator, date);
        Double CurrentvalidTheoryPrice = responsedto.getValidPrice();
        String CurrentvalidTheoryDate = responsedto.getCurrentdate();
        log.info("---------- {}当前有效理论成本价:{},对应日期:{} ---------", indicator, CurrentvalidTheoryPrice,CurrentvalidTheoryDate);

        //获取有效当日电碳现货价
        responsedto = responseM.findValidTheoryPrice("ECSpot", date);
        Double CurrentvalidTheoryPrice_ECSpot = responsedto.getValidPrice();
        String CurrentvalidTheoryDate_ECSpot = responsedto.getCurrentdate();
        log.info("---------- {}当前有效电碳现货价:{},对应日期:{} ---------", "ECSpot", CurrentvalidTheoryPrice_ECSpot,CurrentvalidTheoryDate_ECSpot);
        //计算当期利润
        Double profit =  CurrentvalidTheoryPrice_ECSpot - CurrentvalidTheoryPrice;
        log.info("---------- {}当期利润:{} ---------", indicator, profit);

        return String.valueOf(Math.floor(profit));

    }
    
}
