package com.kungfuactiongrip.exchange.trade;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public abstract class TradeRule implements Runnable {
    
    private boolean _execute;
    private int _marketID;
    ExecuteState _lastRunState;
    
    TradeRule(){
        this._execute = true;
        _lastRunState = ExecuteState.NOP;
    }
    
    public ExecuteState fetchLastRunState(){
        return _lastRunState;
    }
    
    public void setMarketID(int marketID){
        _marketID = marketID;
    }
    
    public void shutdown(){
        _execute = false;
    }

    public abstract void runImpl();
    
    @Override
    public void run() {
        
        if(_marketID <= 0){
            this.shutdown();
            _lastRunState = ExecuteState.RULE_RUN_ABORTED;
        }
        
        while(_execute){
            runImpl();
            
            // run and then sleep for a bit ;)
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(TradeRule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
