/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.config.exchange.PropertyBagFactory;
import com.kungfuactiongrip.exchange.io.data.DBProviderFactory;
import com.kungfuactiongrip.exchange.io.data.IDbDAO;
import com.kungfuactiongrip.exchange.io.data.MySQLDBObject;
import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
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
    public List<BuyOrder> FetchOpenBuyOrdersForMarket(int marketID) {
        return new ArrayList<>();
    }

    @Override
    public List<SellOrder> FetchOpenSellOrdersForMarket(int marketID) {
        return new ArrayList<>();
    }

    
}
