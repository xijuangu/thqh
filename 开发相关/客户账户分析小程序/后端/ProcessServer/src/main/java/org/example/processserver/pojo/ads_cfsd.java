package org.example.processserver.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2024/11/5
 * @Description dws_futures_client_finance_status 客户资金状况dws
 */
@Component
@Data
public class ads_cfsd {
    @CsvBindByName(column = "client_id")
    private String client_id;//用户id
    @CsvBindByName(column = "busi_date")
    private String busi_date;//交易日
    @CsvBindByName(column = "yes_rights")
    private double yes_rights;//期初权益
    @CsvBindByName(column = "rights")
    private double rights;//期末权益
    @CsvBindByName(column = "market_deposit")
    private double market_deposit;//市值权益
    @CsvBindByName(column = "trading_profit_and_loss")
    private double trading_profit_and_loss;//交易盈亏
    @CsvBindByName(column = "charge")
    private double charge;//手续费
    @CsvBindByName(column = "net_deposit")
    private double net_deposit;//净入金
    @CsvBindByName(column = "opt_premium_income")
    private double opt_premium_income;//权利金收入
    @CsvBindByName(column = "opt_premium_pay")
    private double opt_premium_pay;//权利金支出
    @CsvBindByName(column = "profit_days")
    private int profit_days;//盈利天数
    @CsvBindByName(column = "loss_days")
    private int loss_days;//亏损天数
    @CsvBindByName(column = "turnover")
    private double turnover;//成交量
    @CsvBindByName(column = "risk_level")
    private double risk_level;//风险度
    @CsvBindByName(column = "today_net_value")
    private double today_net_value;//当日净值
    @CsvBindByName(column = "trade_day_flag")
    Integer trade_day_flag;//交易日标识

    public ads_cfsd() {
    }

    public ads_cfsd(String client_id, String busi_date, double yes_rights, double rights, double market_deposit, double trading_profit_and_loss, double charge, double net_deposit, double opt_premium_income, double opt_premium_pay, int profit_days, int loss_days, double turnover, double risk_level, double today_net_value, int trade_day_flag) {
        this.client_id = client_id;
        this.busi_date = busi_date;
        this.yes_rights = yes_rights;
        this.rights = rights;
        this.market_deposit = market_deposit;
        this.trading_profit_and_loss = trading_profit_and_loss;
        this.charge = charge;
        this.net_deposit = net_deposit;
        this.opt_premium_income = opt_premium_income;
        this.opt_premium_pay = opt_premium_pay;
        this.profit_days = profit_days;
        this.loss_days = loss_days;
        this.turnover = turnover;
        this.risk_level = risk_level;
        this.today_net_value = today_net_value;
        this.trade_day_flag = trade_day_flag;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getBusi_date() {
        return busi_date;
    }

    public void setBusi_date(String busi_date) {
        this.busi_date = busi_date;
    }

    public double getYes_rights() {
        return yes_rights;
    }

    public void setYes_rights(double yes_rights) {
        this.yes_rights = yes_rights;
    }

    public double getRights() {
        return rights;
    }

    public void setRights(double rights) {
        this.rights = rights;
    }

    public double getMarket_deposit() {
        return market_deposit;
    }

    public void setMarket_deposit(double market_deposit) {
        this.market_deposit = market_deposit;
    }

    public double getTrading_profit_and_loss() {
        return trading_profit_and_loss;
    }

    public void setTrading_profit_and_loss(double trading_profit_and_loss) {
        this.trading_profit_and_loss = trading_profit_and_loss;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getNet_deposit() {
        return net_deposit;
    }

    public void setNet_deposit(double net_deposit) {
        this.net_deposit = net_deposit;
    }

    public double getOpt_premium_income() {
        return opt_premium_income;
    }

    public void setOpt_premium_income(double opt_premium_income) {
        this.opt_premium_income = opt_premium_income;
    }

    public double getOpt_premium_pay() {
        return opt_premium_pay;
    }

    public void setOpt_premium_pay(double opt_premium_pay) {
        this.opt_premium_pay = opt_premium_pay;
    }

    public int getProfit_days() {
        return profit_days;
    }

    public void setProfit_days(int profit_days) {
        this.profit_days = profit_days;
    }

    public int getLoss_days() {
        return loss_days;
    }

    public void setLoss_days(int loss_days) {
        this.loss_days = loss_days;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(double risk_level) {
        this.risk_level = risk_level;
    }

    public double getToday_net_value() {
        return today_net_value;
    }

    public void setToday_net_value(double today_net_value) {
        this.today_net_value = today_net_value;
    }

    public int getTrade_day_flag() {
        return trade_day_flag;
    }

    public void setTrade_day_flag(int trade_day_flag) {
        this.trade_day_flag = trade_day_flag;
    }

    @Override
    public String toString() {
        return "ads_cfsd{" +
                "client_id='" + client_id + '\'' +
                ", busi_date='" + busi_date + '\'' +
                ", yes_rights=" + yes_rights +
                ", rights=" + rights +
                ", market_deposit=" + market_deposit +
                ", trading_profit_and_loss=" + trading_profit_and_loss +
                ", charge=" + charge +
                ", net_deposit=" + net_deposit +
                ", opt_premium_income=" + opt_premium_income +
                ", opt_premium_pay=" + opt_premium_pay +
                ", profit_days=" + profit_days +
                ", loss_days=" + loss_days +
                ", turnover=" + turnover +
                ", risk_level=" + risk_level +
                ", today_net_value=" + today_net_value +
                ", trade_day_flag=" + trade_day_flag +
                '}';
    }


    public Object setData(String[] str, ads_cfsd adsCftd) {
        if(str[0] != ""){
            adsCftd.setClient_id(str[0]);
        }
        if(str[1] != ""){
            adsCftd.setBusi_date(str[1]);
        }
        if(str[2] != null && !str[2].trim().isEmpty()){
            adsCftd.setYes_rights(Double.parseDouble(str[2].trim()));
        }
        if(str[3] != null && !str[3].trim().isEmpty()){
            adsCftd.setRights(Double.parseDouble(str[3].trim()));
        }
        if(str[4] != null && !str[4].trim().isEmpty()){
            adsCftd.setMarket_deposit(Double.parseDouble(str[4].trim()));
        }
        if(str[5] != null && !str[5].trim().isEmpty()){
            adsCftd.setTrading_profit_and_loss(Double.parseDouble(str[5].trim()));
        }
        if(str[6] != null && !str[6].trim().isEmpty()){
            adsCftd.setCharge(Double.parseDouble(str[6].trim()));
        }
        if(str[7] != null && !str[7].trim().isEmpty()){
            adsCftd.setNet_deposit(Double.parseDouble(str[7].trim()));
        }
        if(str[8] != null && !str[8].trim().isEmpty()){
            adsCftd.setOpt_premium_income(Double.parseDouble(str[8].trim()));
        }
        if(str[9] != null && !str[9].trim().isEmpty()){
            adsCftd.setOpt_premium_pay(Double.parseDouble(str[9].trim()));
        }
        if(str[10] != null && !str[10].trim().isEmpty()){
            adsCftd.setProfit_days(Integer.parseInt(str[10].trim()));
        }
        if(str[11] != null && !str[11].trim().isEmpty()){
            adsCftd.setLoss_days(Integer.parseInt(str[11].trim()));
        }
        if(str[12] != null && !str[12].trim().isEmpty()){
            adsCftd.setTurnover(Double.parseDouble(str[12].trim()));
        }
        if(str[13] != null && !str[13].trim().isEmpty()){
            adsCftd.setRisk_level(Double.parseDouble(str[13].trim()));
        }
        if(str[14] != null && !str[14].trim().isEmpty()){
            adsCftd.setToday_net_value(Double.parseDouble(str[14].trim()));
        }
        if(str[15] != null && !str[15].trim().isEmpty()){
            adsCftd.setTrade_day_flag(Integer.parseInt(str[15].trim()));
        }

        return adsCftd;
    }
}
