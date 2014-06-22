package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.data.DBProviderFactory;
import com.kungfuactiongrip.exchange.io.data.IDbDAO;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class BotIOImpl implements IBotIO {

   private IDbDAO _dbObject;
    
    BotIOImpl() {
        this(null);
    }
    
    /**
     *
     * @param propertyFileName
     */
    BotIOImpl(String propertyFileName){
        
        if(propertyFileName != null){
            _dbObject = DBProviderFactory.GenerateMySQLDBObject(propertyFileName);
        }
    }

    @Override
    public String FetchUser(){
        
        if(_dbObject != null){
            return _dbObject.FetchUser();
        }
        
        return "";
    }

    @Override
    public List<TradeOrder> FetchOpenBuyOrdersForMarket(int marketID, ExchangeList exchange) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrdersForMarket(marketID, TradeType.BUY, TradeState.OPEN, exchange);
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<TradeOrder> FetchOpenSellOrdersForMarket(int marketID, ExchangeList exchange) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrdersForMarket(marketID, TradeType.SELL, TradeState.OPEN, exchange);
        }
        
        return new ArrayList<>();
    }

    @Override
    public int InsertOrder(TradeType type, TradeState state, ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID, String linkedID) {
        
        if(_dbObject != null){
            return _dbObject.InsertOrder(type, state, exchange, marketID, pricePer, totalValue, tradeID, linkedID);
        }
        
        return -1;
    }

    @Override
    public int InsertOrder(TradeType type, TradeState state, ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID) {
        
        if(_dbObject != null){
            return _dbObject.InsertOrder(type, state, exchange, marketID, pricePer, totalValue, tradeID, null);
        }
        
        return -1;
    }
    
    @Override
    public boolean UpdateOrderState(int rowID, TradeState state) {
        
        if(_dbObject != null && rowID > 0){
            return _dbObject.UpdateOrderState(rowID, state);
        }
        
        return false;
    }

    @Override
    public int FetchNumberOfOpenBuyOrdersForMarketForInterval(int marketID, ExchangeList exchange, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.OPEN, exchange, marketID, hourInterval);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfOpenSellOrdersForMarketForInterval(int marketID, ExchangeList exchange, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.OPEN, exchange, marketID, hourInterval);
        }
        
        return -1;
    }
    
    @Override
    public int FetchNumberOfOpenBuyOrdersForMarketForDay(int marketID, ExchangeList exchange) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.OPEN, exchange, marketID, 24);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfOpenSellOrdersForMarketForDay(int marketID, ExchangeList exchange) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.OPEN, exchange, marketID, 24);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedSellOrdersForInterval(int marketID, ExchangeList exchange, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.ABORTED, exchange, marketID, hourInterval);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedBuyOrdersForInterval(int marketID, ExchangeList exchange, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.ABORTED, exchange, marketID, hourInterval);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedSellOrdersForDay(int marketID, ExchangeList exchange) {
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.ABORTED, exchange, marketID, 24);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedBuyOrdersForDay(int marketID, ExchangeList exchange) {
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.ABORTED, exchange, marketID, 24);
        }
        
        return -1;
    }
}
