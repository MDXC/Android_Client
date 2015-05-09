package com.test.juxiaohui.common.dal;

/**
 * Created by yihao on 15/5/9.
 */
public interface IUtilServer {

    /**
     * 计算汇率
     * @param srcCurrency 源货币
     * @param destCurrency 目标货币
     * @return
     */
    public float getExchangeRate(String srcCurrency, String destCurrency);


}
