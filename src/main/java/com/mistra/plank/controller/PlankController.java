package com.mistra.plank.controller;

import com.mistra.plank.job.StockProcessor;
import com.mistra.plank.model.param.AutoTradeParam;
import com.mistra.plank.model.param.FundHoldingsParam;
import com.mistra.plank.model.param.SelfSelectParam;
import com.mistra.plank.service.StockSelectedService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author mistra@future.com
 * @date 2021/11/19
 */
@RestController
public class PlankController {

    private final StockProcessor stockProcessor;
    private final StockSelectedService stockSelectedService;

    public PlankController(StockProcessor stockProcessor, StockSelectedService stockSelectedService) {
        this.stockProcessor = stockProcessor;
        this.stockSelectedService = stockSelectedService;
    }

    /**
     * 导入基金季度持仓数据
     * 基金季度持仓数据导出步骤：
     * Choice金融客户端：基金->资产配置->重仓持股(汇总)->选择某个季度->提取数据->导出到Excel
     *
     * @param fundHoldingsParam FundHoldingsParam
     * @param beginTime         季度开始时间
     * @param endTime           季度结束时间
     */
    @PostMapping("/fund-holdings/{beginTime}/{endTime}")
    public void fundHoldingsImport(FundHoldingsParam fundHoldingsParam,
                                   @PathVariable(value = "beginTime") Long beginTime, @PathVariable(value = "endTime") Long endTime) {
        stockProcessor.fundHoldingsImport(fundHoldingsParam, new Date(beginTime), new Date(endTime));
    }

    /**
     * 编辑web页面自选
     *
     * @param selfSelectParam SelfSelectParam
     */
    @PostMapping("/add-self-select")
    public void addSelfSelect(@RequestBody SelfSelectParam selfSelectParam) {
        stockSelectedService.addSelfSelect(selfSelectParam);
    }

    /**
     * 下一个交易日自动交易池
     *
     * @param autoTradeParams autoTradeParams
     */
    @PostMapping("/tomorrow-auto-trade-pool")
    public void tomorrowAutoTradePool(@RequestBody List<AutoTradeParam> autoTradeParams) {
        stockSelectedService.tomorrowAutoTradePool(autoTradeParams);
    }

}
