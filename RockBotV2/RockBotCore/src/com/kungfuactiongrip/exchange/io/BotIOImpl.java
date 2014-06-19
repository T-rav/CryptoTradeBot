/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.data.DBProviderFactory;
import com.kungfuactiongrip.exchange.io.data.IDbDAO;
import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
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

    public String FetchUser(){
        
        if(_dbObject != null){
            return _dbObject.FetchUser();
        }
        
        return "";
    }

    @Override
    public List<TradeOrder> FetchOpenBuyOrdersForMarket(int marketID, ExchangeList exchange) {
        
        if(_dbObject != null){
            return _dbObject.FetchOpenBuyOrdersForMarket(marketID, exchange);
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<TradeOrder> FetchOpenSellOrdersForMarket(int marketID, ExchangeList exchange) {
        if(_dbObject != null){
            return _dbObject.FetchOpenSellOrdersForMarket(marketID, exchange);
        }
        
        return new ArrayList<>();
    }

    @Override
    public boolean InsertOrder(TradeType type, TradeState state, ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID, String linkedID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean UpdateOrderState(int rowID, TradeState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
