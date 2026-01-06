package org.example.processserver.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2024/11/5
 * @Description ads_futures_client_finance_status 客户资金状况ADS
 */
@Component
@Data
public class ads_cfs {
    @CsvBindByName(column = "client_id")
    private String client_id;//用户id
    @CsvBindByName(column = "period")
    private String period;//期间
    @CsvBindByName(column = "begin_busi_date")
    private String begin_busi_date;//起始交易日
    @CsvBindByName(column = "end_busi_date")
    private String end_busi_date;//结束交易日
    @CsvBindByName(column = "yes_rights")
    private Double yes_rights;//期初权益
    @CsvBindByName(column = "rights")
    private Double rights;//期末权益
    @CsvBindByName(column = "market_deposit")
    private Double market_deposit;//市值权益
    @CsvBindByName(column = "trading_profit_and_loss")
    private Double trading_profit_and_loss;//交易盈亏
    @CsvBindByName(column = "charge")
    private Double charge;//手续费
    @CsvBindByName(column = "net_profit")
    private Double net_profit;//净利润
    @CsvBindByName(column = "net_deposit")
    private Double net_deposit;//净入金
    @CsvBindByName(column = "opt_premium_income")
    private Double opt_premium_income;//权利金收入
    @CsvBindByName(column = "opt_premium_pay")
    private Double opt_premium_pay;//权利金支出
    @CsvBindByName(column = "profit_days")
    private Integer profit_days;//盈利天数
    @CsvBindByName(column = "loss_days")
    private Integer loss_days;//亏损天数
    @CsvBindByName(column = "trade_win_rate")
    private Double trade_win_rate;//交易胜率
    @CsvBindByName(column = "turnover")
    private Double turnover;//成交量
    @CsvBindByName(column = "sharpe_ratio")
    private Double sharpe_ratio;//夏普比率
    @CsvBindByName(column = "yield_rate")
    private Double yield_rate;//累计收益率
    @CsvBindByName(column = "trade_days")
    private Integer trade_days;//交易天数

    public ads_cfs() {
    }

    public ads_cfs(String client_id, String period, String begin_busi_date, String end_busi_date, Double yes_rights, Double rights, Double market_deposit, Double trading_profit_and_loss, Double charge, Double net_profit, Double net_deposit, Double opt_premium_income, Double opt_premium_pay, Integer profit_days, Integer loss_days, Double trade_win_rate, Double turnover, Double sharpe_ratio, Double yield_rate, Integer trade_days) {
        this.client_id = client_id;
        this.period = period;
        this.begin_busi_date = begin_busi_date;
        this.end_busi_date = end_busi_date;
        this.yes_rights = yes_rights;
        this.rights = rights;
        this.market_deposit = market_deposit;
        this.trading_profit_and_loss = trading_profit_and_loss;
        this.charge = charge;
        this.net_profit = net_profit;
        this.net_deposit = net_deposit;
        this.opt_premium_income = opt_premium_income;
        this.opt_premium_pay = opt_premium_pay;
        this.profit_days = profit_days;
        this.loss_days = loss_days;
        this.trade_win_rate = trade_win_rate;
        this.turnover = turnover;
        this.sharpe_ratio = sharpe_ratio;
        this.yield_rate = yield_rate;
        this.trade_days = trade_days;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBegin_busi_date() {
        return begin_busi_date;
    }

    public void setBegin_busi_date(String begin_busi_date) {
        this.begin_busi_date = begin_busi_date;
    }

    public String getEnd_busi_date() {
        return end_busi_date;
    }

    public void setEnd_busi_date(String end_busi_date) {
        this.end_busi_date = end_busi_date;
    }

    public Double getYes_rights() {
        return yes_rights;
    }

    public void setYes_rights(Double yes_rights) {
        this.yes_rights = yes_rights;
    }

    public Double getRights() {
        return rights;
    }

    public void setRights(Double rights) {
        this.rights = rights;
    }

    public Double getMarket_deposit() {
        return market_deposit;
    }

    public void setMarket_deposit(Double market_deposit) {
        this.market_deposit = market_deposit;
    }

    public Double getTrading_profit_and_loss() {
        return trading_profit_and_loss;
    }

    public void setTrading_profit_and_loss(Double trading_profit_and_loss) {
        this.trading_profit_and_loss = trading_profit_and_loss;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Double getNet_profit() {
        return net_profit;
    }

    public void setNet_profit(Double net_profit) {
        this.net_profit = net_profit;
    }

    public Double getNet_deposit() {
        return net_deposit;
    }

    public void setNet_deposit(Double net_deposit) {
        this.net_deposit = net_deposit;
    }

    public Double getOpt_premium_income() {
        return opt_premium_income;
    }

    public void setOpt_premium_income(Double opt_premium_income) {
        this.opt_premium_income = opt_premium_income;
    }

    public Double getOpt_premium_pay() {
        return opt_premium_pay;
    }

    public void setOpt_premium_pay(Double opt_premium_pay) {
        this.opt_premium_pay = opt_premium_pay;
    }

    public Integer getProfit_days() {
        return profit_days;
    }

    public void setProfit_days(Integer profit_days) {
        this.profit_days = profit_days;
    }

    public Integer getLoss_days() {
        return loss_days;
    }

    public void setLoss_days(Integer loss_days) {
        this.loss_days = loss_days;
    }

    public Double getTrade_win_rate() {
        return trade_win_rate;
    }

    public void setTrade_win_rate(Double trade_win_rate) {
        this.trade_win_rate = trade_win_rate;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public Double getSharpe_ratio() {
        return sharpe_ratio;
    }

    public void setSharpe_ratio(Double sharpe_ratio) {
        this.sharpe_ratio = sharpe_ratio;
    }

    public Double getYield_rate() {
        return yield_rate;
    }

    public void setYield_rate(Double yield_rate) {
        this.yield_rate = yield_rate;
    }

    public Integer getTrade_days() {
        return trade_days;
    }

    public void setTrade_days(Integer trade_days) {
        this.trade_days = trade_days;
    }

    @Override
    public String toString() {
        return "ads_cfs{" +
                "client_id='" + client_id + '\'' +
                ", period='" + period + '\'' +
                ", begin_busi_date='" + begin_busi_date + '\'' +
                ", end_busi_date='" + end_busi_date + '\'' +
                ", yes_rights=" + yes_rights +
                ", rights=" + rights +
                ", market_deposit=" + market_deposit +
                ", trading_profit_and_loss=" + trading_profit_and_loss +
                ", charge=" + charge +
                ", net_profit=" + net_profit +
                ", net_deposit=" + net_deposit +
                ", opt_premium_income=" + opt_premium_income +
                ", opt_premium_pay=" + opt_premium_pay +
                ", profit_days=" + profit_days +
                ", loss_days=" + loss_days +
                ", trade_win_rate=" + trade_win_rate +
                ", turnover=" + turnover +
                ", sharpe_ratio=" + sharpe_ratio +
                ", yield_rate=" + yield_rate +
                ", trade_days=" + trade_days +
                '}';
    }

    public Object setData(String[] str, ads_cfs adsCfs) {
        if (str[0] != "") {
            adsCfs.setClient_id(str[0]);
        }
        if (str[1] != "") {
            adsCfs.setPeriod(str[1]);
        }
        if (str[2] != "") {
            adsCfs.setBegin_busi_date(str[2]);
        }
        if (str[3] != "") {
            adsCfs.setEnd_busi_date(str[3]);
        }
        if (str[4] != null && !str[4].trim().isEmpty()) {
            adsCfs.setYes_rights(Double.parseDouble(str[4].trim()));
        }
        if (str[5] != null && !str[5].trim().isEmpty()) {
            adsCfs.setRights(Double.parseDouble(str[5].trim()));
        }
        if (str[6] != null && !str[6].trim().isEmpty()) {
            adsCfs.setMarket_deposit(Double.parseDouble(str[6].trim()));
        }
        if (str[7] != null && !str[7].trim().isEmpty()) {
            adsCfs.setTrading_profit_and_loss(Double.parseDouble(str[7].trim()));
        }
        if (str[8] != null && !str[8].trim().isEmpty()) {
            adsCfs.setCharge(Double.parseDouble(str[8].trim()));
        }
        if (str[9] != null && !str[9].trim().isEmpty()) {
            adsCfs.setNet_profit(Double.parseDouble(str[9].trim()));
        }
        if (str[10] != null && !str[10].trim().isEmpty()) {
            adsCfs.setNet_deposit(Double.parseDouble(str[10].trim()));
        }
        if (str[11] != null && !str[11].trim().isEmpty()) {
            adsCfs.setOpt_premium_income(Double.parseDouble(str[11].trim()));
        }
        if (str[12] != null && !str[12].trim().isEmpty()) {
            adsCfs.setOpt_premium_pay(Double.parseDouble(str[12].trim()));
        }
        if (str[13] != null && !str[13].trim().isEmpty()) {
            adsCfs.setProfit_days(Integer.parseInt(str[13].trim()));
        }
        if (str[14] != null && !str[14].trim().isEmpty()) {
            adsCfs.setLoss_days(Integer.parseInt(str[14].trim()));
        }
        if (str[15] != null && !str[15].trim().isEmpty()) {
            adsCfs.setTrade_win_rate(Double.parseDouble(str[15].trim()));
        }
        if (str[16] != null && !str[16].trim().isEmpty()) {
            adsCfs.setTurnover(Double.parseDouble(str[16].trim()));
        }
        if (str[17] != null && !str[17].trim().isEmpty()) {
            adsCfs.setSharpe_ratio(Double.parseDouble(str[17].trim()));
        }
        if (str[18] != null && !str[18].trim().isEmpty()) {
            adsCfs.setYield_rate(Double.parseDouble(str[18].trim()));
        }
        if (str[19] != null && !str[19].trim().isEmpty()) {
            adsCfs.setTrade_days(Integer.parseInt(str[19].trim()));
        }

        return adsCfs;
    }
}
