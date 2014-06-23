package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.io.data.DBProviderFactory;
import com.kungfuactiongrip.exchange.io.data.IDbDAO;
import com.kungfuactiongrip.exchange.objects.MarketBuySellOrders;
import com.kungfuactiongrip.exchange.objects.MarketTrade;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class BotIOImpl implements IBotIO {

    private IDbDAO _dbObject;
    private ExchangeList _exchangeName;
    private IExchange _exchangeObj;
    
    BotIOImpl() {
        this(null, null);
    }
    
    /**
     *
     * @param propertyFileName
     */
    BotIOImpl(String propertyFileName, ExchangeList exchange){
        
        if(propertyFileName != null){
            _dbObject = DBProviderFactory.GenerateMySQLDBObject(propertyFileName);
        }
        
        if(exchange != null){
            _exchangeName = exchange;
            _exchangeObj = exchange.GenerateExchangeObject();
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
    public List<TradeOrder> FetchOpenBuyOrdersForMarket(int marketID) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrdersForMarket(marketID, TradeType.BUY, TradeState.OPEN, _exchangeName);
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<TradeOrder> FetchOpenSellOrdersForMarket(int marketID) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrdersForMarket(marketID, TradeType.SELL, TradeState.OPEN, _exchangeName);
        }
        
        return new ArrayList<>();
    }

    @Override
    public int InsertOrder(TradeType type, TradeState state, int marketID, double pricePer, double totalValue, String tradeID, String linkedID) {
        
        if(_dbObject != null){
            return _dbObject.InsertOrder(type, state, _exchangeName, marketID, pricePer, totalValue, tradeID, linkedID);
        }
        
        return -1;
    }

    @Override
    public int InsertOrder(TradeType type, TradeState state, int marketID, double pricePer, double totalValue, String tradeID) {
        
        if(_dbObject != null){
            return _dbObject.InsertOrder(type, state, _exchangeName, marketID, pricePer, totalValue, tradeID, null);
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
    public int FetchNumberOfOpenBuyOrdersForMarketForInterval(int marketID, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.OPEN, _exchangeName, marketID, hourInterval);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfOpenSellOrdersForMarketForInterval(int marketID, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.OPEN, _exchangeName, marketID, hourInterval);
        }
        
        return -1;
    }
    
    @Override
    public int FetchNumberOfOpenBuyOrdersForMarketForDay(int marketID) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.OPEN, _exchangeName, marketID, 24);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfOpenSellOrdersForMarketForDay(int marketID) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.OPEN, _exchangeName, marketID, 24);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedSellOrdersForInterval(int marketID, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.ABORTED, _exchangeName, marketID, hourInterval);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedBuyOrdersForInterval(int marketID, int hourInterval) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.ABORTED, _exchangeName, marketID, hourInterval);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedSellOrdersForDay(int marketID) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.ABORTED, _exchangeName, marketID, 24);
        }
        
        return -1;
    }

    @Override
    public int FetchNumberOfAbortedBuyOrdersForDay(int marketID) {
        
        if(_dbObject != null){
            return _dbObject.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.ABORTED, _exchangeName, marketID, 24);
        }
        
        return -1;
    }

//    @Override
//    public List<MarketTrade> FetchMarketTrades(int marketID) {
//
//        List<MarketTrade> result = new ArrayList<>();
//        if(_exchangeObj != null){
//            try {
//                return _exchangeObj.FetchMarketTrades(marketID);
//            } catch (Exception ex) {
//                Logger.getLogger(BotIOImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        
//        return null;
//    }
    
     @Override
    public MarketBuySellOrders FetchMarketOrders(int marketID) {

        MarketBuySellOrders result = null;
        if(_exchangeObj != null){
            try {
                return _exchangeObj.FetchMarketOrders(marketID);
            } catch (Exception ex) {
                Logger.getLogger(BotIOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
}
