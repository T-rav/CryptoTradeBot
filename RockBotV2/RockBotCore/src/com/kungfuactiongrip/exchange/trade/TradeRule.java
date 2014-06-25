package com.kungfuactiongrip.exchange.trade;

/**
 *
 * @author Administrator
 */
public abstract class TradeRule implements Runnable {
    
    private boolean _execute;
    private int _marketID;
    
    TradeRule(){
        this._execute = true;
    }
    
    public void setMarketID(int marketID){
        _marketID = marketID;
    }
    
    public void shutdown(){
        _execute = false;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
