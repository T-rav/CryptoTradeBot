package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.io.IBotIO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public abstract class TradeRule implements Runnable {
    
    private boolean _execute;
    
    // Core members for any trade rule ;)
    int _marketID;
    ExecuteState _lastRunState;
    IExchange _exchangeObj;
    IBotIO _dbObj;
    
    TradeRule(){
        this._execute = true;
        _lastRunState = ExecuteState.NOP;
    }
    
    void setExchange(IExchange exchange){
        _exchangeObj = exchange;
    }
    
    void setDB(IBotIO dbObj){
        _dbObj = dbObj;
    }
    
    void setMarketID(int marketID){
        _marketID = marketID;
    }
    
    public ExecuteState fetchLastRunState(){
        return _lastRunState;
    }

    public void shutdown(){
        _execute = false;
    }

    public abstract void runImpl();
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        
        if(_marketID <= 0 || _dbObj == null || _exchangeObj == null){
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
