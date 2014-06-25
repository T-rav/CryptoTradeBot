package com.kungfuactiongrip.exchange.trade;

/**
 *
 * @author Administrator
 */
public enum ExecuteState {
    
    CREATED_BUY_TRADE,
    ABORTED_BUY_TRADE,
    CLOSED_BUY_TRADE,
    CREATED_SELL_TRADE,
    ABORTED_SELL_TRADE,
    CLOSED_SELL_TRADE,
    RULE_RUN_ABORTED,
    NOP
}
