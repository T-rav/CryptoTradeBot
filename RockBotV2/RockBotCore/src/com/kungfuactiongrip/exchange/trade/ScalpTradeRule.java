package com.kungfuactiongrip.exchange.trade;

/**
 *
 * @author Administrator
 */
class ScalpTradeRule extends TradeRule {

    ScalpTradeRule() {
    }    

    @Override
    public void runImpl() {
        this._lastRunState = ExecuteState.NOP;
    }
}
