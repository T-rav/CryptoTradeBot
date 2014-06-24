/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.kungfuactiongrip.exchange.io.BotIOImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class ObjectConverter {

    private final Gson g = new Gson();
    public ObjectConverter() {
    }
    
    public MarketBalanceList MakeMarketBalanceList(String payload){
        MarketBalanceList result  = new MarketBalanceList();
        
        try{
            JSONObject obj = JsonPath.read(payload, "$.return.balances_available[*]");
            Set<String> keys = obj.keySet();
            for(String key : keys){
                Object balance = obj.get(key);
                result.AddBalance(key, Double.parseDouble(balance.toString()));
            }
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
        }
        
        return result;
    }
    
    public MarketCreateOrderResult MakeMarketOrderCreateResultObject(String payload){
        try{
            String obj = JsonPath.read(payload, "$.success");
            
            // error ;(
            if(obj.equals("0")){
                String error = JsonPath.read(payload, "$.error");
                return new MarketCreateOrderResult(error);
            }
            
            return g.fromJson(payload, MarketCreateOrderResult.class);
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
            return null;
        }
    }
    
    public MarketTradeFee MakeTradeFeeObject(String payload){
        MarketTradeFee result = null;

        // process sell orders
        try{
            JSONObject obj = JsonPath.read(payload, "$.return[*]");

            Object fee = obj.get("fee");
            Object net = obj.get("net");

            result = new MarketTradeFee(Double.parseDouble(fee.toString()), Double.parseDouble(net.toString()));
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
            return null;
        }
        
        return result;
    }
    
    public MarketCancelOrderResult MakeCancelOrderResultObject(String payload){

        try{
            String obj = JsonPath.read(payload, "$.success");
            
            // error ;(
            if(obj.equals("0")){
                String error = JsonPath.read(payload, "$.error");
                return new MarketCancelOrderResult(error, true);
            }else{
                String error = JsonPath.read(payload, "$.return");
                return new MarketCancelOrderResult(error, false);
            }
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
        }
        
        return null;
    }
    
    public List<MarketOpenOrder> MakeMyOpenOrderList(String payload){
        List<MarketOpenOrder> result = new ArrayList<>();
        
        Gson json = new Gson();
        
        try{
            // process sell orders
            JSONArray obj = JsonPath.read(payload, "$.return[*]");
            for(Object e : obj){
                MarketOpenOrder mo = json.fromJson(e.toString(), MarketOpenOrder.class);
                // Add to collection ;)
                result.add(mo);
            }
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
        }
        
        return result;
    }
    
    public MarketBuySellOrders MakeMarketOrderList(String payload){
        MarketBuySellOrders result = new MarketBuySellOrders();
        Gson json = new Gson();
        
        try{
            // process sell orders
            JSONArray obj = JsonPath.read(payload, "$.return.sellorders[*]");
            for(Object e : obj){
                MarketSellOrder mo = json.fromJson(e.toString(), MarketSellOrder.class);
                // Add to collection ;)
                result.SellOrders.add(mo);
            }

            // process buy orders
            obj = JsonPath.read(payload, "$.return.buyorders[*]");
            for(Object e : obj){
                MarketBuyOrder mo = json.fromJson(e.toString(), MarketBuyOrder.class);
                // Add to collection ;)
                result.BuyOrders.add(mo);
            }
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
            return null;
        }
        
        return result;
    }

    public List<MarketTrade> MakeMarketTradeList(String payload) {
        List<MarketTrade> result = new ArrayList<>();
        Gson json = new Gson();
        try{
            JSONArray obj = JsonPath.read(payload, "$.return[*]");
            for(Object e : obj){
                MarketTrade mt = json.fromJson(e.toString(), MarketTrade.class);
                // Add to collection ;)
                result.add(mt);
            }
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
            return null;
        }
        
        return result;
    }
    
    public List<MarketTradeVerbose> MakeMyTradeList(String payload){
        List<MarketTradeVerbose> result = new ArrayList<>();
        Gson json = new Gson();
        try{
            JSONArray obj = JsonPath.read(payload, "$.return[*]");
            for(Object e : obj){
                MarketTradeVerbose mt = json.fromJson(e.toString(), MarketTradeVerbose.class);
                // Add to collection ;)
                result.add(mt);
            }
        }catch(Exception e){
            Logger.getLogger(ObjectConverter.class.getName()).log(Level.WARNING, null, e);
            return null;
        }
        
        return result;
    }  
}
