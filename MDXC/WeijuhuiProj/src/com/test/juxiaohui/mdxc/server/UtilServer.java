package com.test.juxiaohui.mdxc.server;

import com.test.juxiaohui.common.dal.IUtilServer;

/**
 * Created by yihao on 15/5/9.
 */
public class UtilServer implements IUtilServer {
    /**
     * 计算汇率
     *
     * @param srcCurrency  源货币
     * @param destCurrency 目标货币
     * @return
     */
    @Override
    public float getExchangeRate(String srcCurrency, String destCurrency) {
        return 1;
    }
}
