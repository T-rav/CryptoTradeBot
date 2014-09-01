package com.kungfuactiongrip.exchange.trade;

/**
 *
 * @author Administrator
 */
public enum ExecuteState {
    
    CREATED_BUY_ORDER,
    ABORTED_BUY_ORDER,
    HOLD_BUY_ORDER,
    CLOSED_BUY_ORDER,
    CREATED_SELL_ORDER,
    ABORTED_SELL_ORDER,
    HOLD_SELL_ORDER,
    CLOSED_SELL_ORDER,
    RULE_RUN_ABORTED,
    EXCEPTION_BUY_ORDER,
    EXCEPTION_SELL_ORDER,
    NOP
}
